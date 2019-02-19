package bomberMan.game;

import bomberMan.game.gameObjects.*;

import java.io.Serializable;
import java.util.Random;

public class LevelManager  implements Serializable {
    User user;
    private int row, column;
    private Random random = new Random();
    private int totalLvlEnemies;

//    private final double enemyLvl4Percentage = 5;
//    private final double enemyLvl3Percentage = 10;
//    private final double enemyLvl2Percentage = 20;


    GameBoard createLevel(int levelNumber) {
        if (user == null) throw new NullPointerException("User has not been set.");

        this.row = user.getRow();
        this.column = user.getColumn();

        GameBoard newGameBoard = new GameBoard(row,column);


        totalLvlEnemies = Math.min(row,column);
        spawnWall(newGameBoard, 17);
        spawnEnemies(newGameBoard, 30, totalLvlEnemies);
        spawnTokens(newGameBoard, Math.min(2*newGameBoard.getEnemies().size(), newGameBoard.getTotalWalls()/3));

        Player player = new Player(user,newGameBoard);
        newGameBoard.addPlayer(player,newGameBoard.getBlock(1,1));

//        newGameBoard.addEnemy(new EnemyLvl3(newGameBoard),newGameBoard.getBlock(3,2));

        newGameBoard.setLvlManager(this);
        return newGameBoard;
    }

    private void spawnEnemies(GameBoard gameBoard, int lvl, int totalEnemies) {
        int rows = gameBoard.getRows(),
                columns = gameBoard.getColumns();
        totalEnemies = lvl > 4 ? (int) (Math.pow(1.05, lvl - 4) * totalEnemies) : totalEnemies;
        int addedEnemies = 0;

        int numberOfTries = 0;
        while (totalEnemies != 0 && numberOfTries < 100*(totalEnemies - addedEnemies)){

            Block block = gameBoard.getBlock(random.nextInt(rows),random.nextInt(columns));

            ObjectID id;
            int rnd = random.nextInt(100);
            if (rnd < 5 && lvl >= 4) id = ObjectID.Enemylvl4;
            else if (rnd < 15 && lvl >= 3) id = ObjectID.Enemylvl3;
            else if (rnd < 35 && lvl >= 2) id = ObjectID.Enemylvl2;
            else id = ObjectID.Enemylvl1;

            if (id == ObjectID.Enemylvl4) {
                if (block.getID() != ObjectID.BorderBlock
                        && !(block.getRow() <= 4 && block.getColumn() <= 4)){
                    gameBoard.addEnemy(new EnemyLvl4(gameBoard),block);
                    addedEnemies++;
                    numberOfTries = 0;
                } else {
                    numberOfTries++;
                }
            } else {
                if (block.getID() == ObjectID.Passage
                        && !(block.getRow() <= 3 && block.getColumn() <= 3)){
                    switch (id){
                        case Enemylvl1: gameBoard.addEnemy(new EnemyLvl1(gameBoard),block); break;
                        case Enemylvl2: gameBoard.addEnemy(new EnemyLvl2(gameBoard),block); break;
                        case Enemylvl3: gameBoard.addEnemy(new EnemyLvl3(gameBoard),block); break;
                    }
                    addedEnemies++;
                    numberOfTries = 0;
                } else {
                    numberOfTries++;
                }
            }
        }
    }

    /**
     *  Spawns random walls to the given gameBoard.
     *  Total number of walls is a fraction of total number of blocks
     *  which is determined by percentage parameter.
     *  If spawning the last few blocks randomly take too much effort they
     *  will not be placed
     *  Ex: if the number of walls is desired to be 10 percent of total
     *  gameBoard blocks: spawnWall(gameBoard,10);
     * @param gameBoard which the walls will be spawned to
     * @param percentage of total gameBoard blocks
     */
    private void spawnWall(GameBoard gameBoard,double percentage){
        int rows = gameBoard.getRows(),
                columns = gameBoard.getColumns();
        int totalBlocks = rows * columns;
        int remainingWalls = (int) (percentage * (totalBlocks) / 100);
        int numberOfTries = 0;
        while (remainingWalls != 0 && numberOfTries < 20*remainingWalls){
            Block block = gameBoard.getBlock(random.nextInt(rows),random.nextInt(columns));
            if (block.getID() == ObjectID.Passage &&
                    !(block.getRow() <= 2 && block.getColumn() <= 2)){
                block.placeWall();
                remainingWalls--;
                numberOfTries = 0;
            } else {
                numberOfTries++;
            }
        }
    }

    private void spawnTokens(GameBoard gameBoard, int total){
        int rows = gameBoard.getRows(),
                columns = gameBoard.getColumns();
        int addedTokens = 0;
        total = total + 1; //counting the door
        int numberOfTries = 0;

        boolean doorAdded = false;
        while (!doorAdded && numberOfTries < 70*(rows*columns)){
            Block block = gameBoard.getBlock(random.nextInt(rows),random.nextInt(columns));
            if (block.getID() == ObjectID.Wall) {
                gameBoard.addToken(new Token(Token.TokenType.Door,block,gameBoard));
                doorAdded = true;
            }else {
                numberOfTries++;
            }
        }
        if (!doorAdded) gameBoard.addToken(new Token(Token.TokenType.Door,gameBoard.getBlock(3,3),gameBoard));


        while (total != 0 && numberOfTries < 20*(total - addedTokens)){

            Block block = gameBoard.getBlock(random.nextInt(rows),random.nextInt(columns));

            if (!block.hasToken() && block.getID() == ObjectID.Wall) {
                Token.TokenType type = null;
                int rnd = random.nextInt(90);
                if (rnd < 5) type = Token.TokenType.GhostMode;
                else if (rnd < 10) type = Token.TokenType.BombControl;
                else if (rnd < 20) type = Token.TokenType.IncreaseBombsLimit;
                else if (rnd < 30) type = Token.TokenType.DecreaseBombsLimit;
                else if (rnd < 40) type = Token.TokenType.IncreaseBombRadius;
                else if (rnd < 50) type = Token.TokenType.DecreaseBombRadius;
                else if (rnd < 60) type = Token.TokenType.IncreaseScore;
                else if (rnd < 70) type = Token.TokenType.DecreaseScore;
                else if (rnd < 80) type = Token.TokenType.IncreaseSpeed;
                else if (rnd < 90) type = Token.TokenType.DecreaseSpeed;

                gameBoard.addToken(new Token(type, block, gameBoard));
                addedTokens++;
                numberOfTries = 0;
            } else {
                numberOfTries++;
            }
        }

    }

    public void setUser(User user) {
        this.user = user;
    }
}
