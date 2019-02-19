package bomberMan.game.gameObjects;

import bomberMan.game.Game;
import bomberMan.game.LevelManager;
import bomberMan.game.Renderable;
import bomberMan.game.Tickable;
import bomberMan.game.menu.Menu;

import java.awt.*;
import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;

public class GameBoard implements Tickable, Renderable, Serializable {

    private LevelManager lvlManager;
    private Rectangle borderBounds;
    private int row, col;
    private static double blockFractionOfHeight = 0.08d;
    private int maxBombRadius;
    private LinkedList<GameObject> backgroundObjects = new LinkedList<>();
    private LinkedList<GameObject> gameObjects = new LinkedList<>();
    private Block[][] backgroundBlocks;
    private Block[][] blocks;
    private LinkedList<Player> players = new LinkedList<>();
    private LinkedList<Enemy> enemies = new LinkedList<>();
    private LinkedList<Bomb> bombs = new LinkedList<>();
    private LinkedList<Token> tokens = new LinkedList<>();


    public GameBoard(int row, int col) {
        this.row = row;
        this.col = col;
        Block.setBlockFractionOfHeight(blockFractionOfHeight);
        maxBombRadius = Math.min(row, col);

        int unit = Block.getHeight();

        //Add extra background blocks
        backgroundBlocks = new Block[Math.max(row,Game.getGameHeight()/Block.getHeight()+1)][Math.max(col,Game.getGameWidth()/Block.getHeight()+1)];
        for (int r = 0; r < backgroundBlocks.length; r++){
            for (int c = 0; c < backgroundBlocks[r].length; c++) {
                if (r >= row | c >= col){
                    backgroundBlocks[r][c] = new Block(c*unit,(r+1)*unit,ObjectID.BorderBlock,r,c,this);
                    backgroundObjects.add(backgroundBlocks[r][c]);
                }
            }
        }

        //Add hardBlocks and border blocks
        blocks = new Block[row][col];
        for (int r = 0; r < this.row; r++) {
            for (int c = 0; c < col; c++) {
                ObjectID id;
                if (r == 0 | c ==0 | r == row-1 | c == col-1) {
                    id = ObjectID.BorderBlock;
                }else if ((c%2 == 0 && r%2 == 0)) {
                    id = ObjectID.HardBlock;
                } else {
                    id = ObjectID.Passage;
                }
                blocks[r][c] = new Block(c*unit,(r+1)*unit,id,r,c,this);
                blocks[r][c].setCurrentBlock(blocks[r][c]);

                gameObjects.add(blocks[r][c]);
            }
        }


        borderBounds = new Rectangle(unit,2*unit,(col-2)*unit,(row-2)*unit);
    }

    @Override
    public void render(Graphics g) {

        //Background
        g.setColor(new Color(74,136,40));
        g.fillRect(0,0,Math.max(Block.getHeight()*col,Game.getGameWidth()),Math.max(Block.getHeight()*(row+1),Game.getGameHeight()));
        for (GameObject backgroundObject : backgroundObjects){
            backgroundObject.render(g);
        }

        //Rendering game objects
        Iterator<GameObject> goIt = gameObjects.iterator();
        while (goIt.hasNext()){
            GameObject gameObject = null;
            try {
                gameObject = goIt.next();
            } catch (ConcurrentModificationException e) {
//                e.printStackTrace();
            }
            gameObject.render(g);
        }

        //border
//        g.setColor(Color.BLUE);
//        int unit = Block.getHeight();
//        g.drawRect(unit,2*unit,(col-2)*unit,(row-2)*unit);


    }

    @Override
    public void tick() {

        for (int i = 0, gameObjectsSize = gameObjects.size(); i < gameObjectsSize; i++) {
            GameObject gameObject = gameObjects.get(i);
            //setting current block field
            if (gameObject.id != ObjectID.HardBlock) {
                gameObject.setCurrentBlock(gameObject.getBlock(this));
            }
            //ticking
            gameObject.tick();
        }
        int aliveEnemies = 0;
        int alivePlayers = 0;
        for (Player player : players) {
            //checking for enemy collision
            for (Enemy enemy : enemies) {
                if (!enemy.dying && player.getBounds().intersects(enemy.getBounds())) {
                    player.die();
                }
                if (!enemy.dying) aliveEnemies++;
            }
            //checking for token achievement
            if (player.currentBlock.hasToken()){
                for (Token token : tokens) {
                    if (!token.dying
                            && token.currentBlock.id == ObjectID.Passage
                            && token.currentBlock.equals(player.currentBlock)){
                        token.affect(player.getUser());
                        player.currentBlock.setHasToken(false);
                        token.die();
                    }
                }
            }
            if (!player.rotted){
                alivePlayers++;
            }
        }
        if (aliveEnemies == 0){
            //ToDo Open the door
        }
        if (alivePlayers == 0){
            Menu.game.newGame();
        }
    }

    /**
     *  Returns a (2*radius+1)*(2*radius+1) <code>Block[][]</code> consisting of all blocks
     *  within the area of <code>blocks[r-radius][c-radius]</code> and <code>blocks[r+radius][c+radius]</code>.
     *  If any of the these blocks don't exist, the related index of returned object will contain <code>null</code>.
     * @param r the row of the block which its surroundings are desired
     * @param c the column of the block which its surroundings are desired
     * @return the surrounding blocks in a (2*radius+1)*(2*radius+1) array of <code>Block</code>
     */
    public Block[][] getSurroundings(int r, int c, int radius){
        Block[][] surroundings = new Block[2*radius+1][2*radius+1];
        for (int sr = -1*radius; sr <= radius; sr++) {
            for (int sc = -1*radius; sc <= radius; sc++) {
                try {
                    surroundings[sr+radius][sc+radius] = blocks[r+sr][c+sc];
                } catch (ArrayIndexOutOfBoundsException e) {
                    surroundings[sr+radius][sc+radius] = null;
                }
            }

        }
        return surroundings;
    }

    public void addPlayer(Player player, Block block) {
        players.add(player);
        gameObjects.add(player);
        player.setLocation(block);
    }

    public void addEnemy(Enemy enemy, Block block){
        enemies.add(enemy);
        gameObjects.add(enemy);
        enemy.setLocation(block);
    }

    public Block getBlock(double centerX, double centerY) {
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                if (blocks[r][c].getBounds().contains(centerX,centerY)){
                    return blocks[r][c];
                }
            }
        }
        return null;
    }

    public Block getBlock(int row, int col){
        return blocks[row][col];
    }

    public LinkedList<Player> getPlayers() {
        return players;
    }

    public int getWidth() {
        return col*Block.getHeight();
    }

    public int getHeight(){
        return (row+1)*Block.getHeight();
    }

    public int getRows() {
        return row;
    }

    public int getColumns() {
        return col;
    }

    /**
     * Returns the sum of horizontal and vertical distance of two given blocks
     * @param block1 first block
     * @param block2 second block
     * @return total horizontal and vertical distance
     */
    public int getDistance(Block block1, Block block2) {
        return Math.abs(block1.getRow()-block2.getRow())
                + Math.abs(block1.getColumn() - block2.getColumn());
    }

    public LinkedList<Enemy> getEnemies() {
        return enemies;
    }

    /**
     *  Returns total number of active bombs of the given player in the game board.
     * @param player given player
     * @return total number of active bombs of the given player in the game board
     */
    public int getTotalBombs(Player player) {
        int total = 0;
        for (Bomb bomb : bombs) {
            if (bomb.getPlayer().equals(player) && !bomb.rotted) total++;
        }
        return total;
    }

    public void putBomb(Bomb bomb) {
        bombs.add(bomb);
        bomb.currentBlock.setHasBomb(true);
        gameObjects.add(row*col + tokens.size() ,bomb);
    }

    public LinkedList<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void addToken(Token token){
        tokens.add(token);
        gameObjects.add(row*col,token);
        token.currentBlock.setHasToken(true);
    }

    public int getTotalWalls() {
        int total = 0;
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                if (blocks[r][c].id == ObjectID.Wall) total++;
            }
        }
        return total;
    }

    public int getMaxBombRadius() {
        return maxBombRadius;
    }

    public LevelManager getLvlManager() {
        return lvlManager;
    }

    public void setLvlManager(LevelManager lvlManager) {
        this.lvlManager = lvlManager;
    }

    public void load() {
        for (GameObject gameObject : gameObjects) {
//            gameObject.setCurrentBlock(gameObject.getBlock(this));
            gameObject.load();
        }
    }
}
