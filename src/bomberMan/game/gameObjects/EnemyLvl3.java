package bomberMan.game.gameObjects;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("Duplicates")
public class EnemyLvl3 extends Enemy implements Serializable {

    private boolean chasing = true;
    private final int exploreTimes = 5;
    private int exploreCount;

    private final int standTimeBlocked = 10;
    private final int standTimeChoosing = 0;
    private int standCounter;
    private Block origin;
    private Block destination;

    public EnemyLvl3(GameBoard gameBoard) {
        super(gameBoard);
        id = ObjectID.Enemylvl1;
        width = (int) (0.6*Block.getHeight());
        height = (int) (0.8*Block.getHeight());
        velX = 2*defaultVelocity;
        velY = velX;
        animationSpeed = 0.1*velX;
        setUpAnimation("enemyLvl3");
    }

    @Override
    public void render(Graphics g) {
        if (!rotted) {
            renderPics(g);
            g.setColor(Color.BLUE);
        }
        g.drawRect((int)(x+Player.renderOffsetX),(int)(y+Player.renderOffsetY), width,height);
    }

    @Override
    public void tick() {
        if (!dying) {
            move();
        }else {
            dyingIterator++;
        }
    }

    /**
     * The moving algorithm of this enemy is described in the following steps:
     *  1. Upon spawning chooses a random direction among the options.
     *  Note that while in chasing mode the blocks which gets the enemy further from the closest player are not an option.
     *  2. Moves in that direction till has another option.
     *  3. Chooses a random direction among options. If non existed, switches to exploring mode
     *  so it will have more options, for a total loop of <code>exploringTimes</code> times.
     *  If it's already in exploring made, stands and does nothing.
     *  4. Goes to 2
     */
    @Override
    protected void move() {
        if (standCounter > 0) {
            standCounter--;
            return;
        }

        Block[][] surroundings = gameBoard.getSurroundings(currentBlock.getRow(), currentBlock.getColumn(), 1);
        if (movingUp || movingDown || movingLeft || movingRight) {
            faceUp = faceLeft = faceRight = faceDown = false;
            moveTillHasOption(surroundings);
            animIterator++;
        } else {

            //Finding non-blocked sides (options)
            List<Block> options = new ArrayList<>();

            boolean canGoRightBlock = this.canMoveRight(surroundings) && surroundings[1][2].isPassable(this);
            boolean canGoLeftBlock = this.canMoveLeft(surroundings) && surroundings[1][0].isPassable(this);
            boolean canGoDownBlock = this.canMoveDown(surroundings) && surroundings[2][1].isPassable(this);
            boolean canGoUpBlock = this.canMoveUp(surroundings) && surroundings[0][1].isPassable(this);

            if (exploreCount >= exploreTimes){
                chasing = true;
                exploreCount = 0;
            }
            if (!chasing) {
                if (canGoUpBlock) options.add(surroundings[0][1]);
                if (canGoLeftBlock) options.add(surroundings[1][0]);
                if (canGoRightBlock) options.add(surroundings[1][2]);
                if (canGoDownBlock) options.add(surroundings[2][1]);
                exploreCount++;
            }else {
                //Finding the closest player
                List<Player> players = gameBoard.getPlayers();
                players.sort(new Comparator<Player>() {
                    @Override
                    public int compare(Player p1, Player p2) {
                        return gameBoard.getDistance(p1.getBlock(gameBoard),currentBlock)
                                - gameBoard.getDistance(p2.getBlock(gameBoard),currentBlock);
                    }
                });
                Block playerBlock = players.get(0).getBlock(gameBoard);
                int distanceToPlayer = gameBoard.getDistance(playerBlock,currentBlock);
                if (canGoUpBlock && gameBoard.getDistance(playerBlock,surroundings[0][1]) < distanceToPlayer) options.add(surroundings[0][1]);
                if (canGoLeftBlock && gameBoard.getDistance(playerBlock,surroundings[1][0]) < distanceToPlayer) options.add(surroundings[1][0]);
                if (canGoRightBlock && gameBoard.getDistance(playerBlock,surroundings[1][2]) < distanceToPlayer) options.add(surroundings[1][2]);
                if (canGoDownBlock && gameBoard.getDistance(playerBlock,surroundings[2][1]) < distanceToPlayer) options.add(surroundings[2][1]);
            }

            origin = currentBlock;
            //Choosing an option side randomly
            try {
                destination = options.get(random.nextInt(options.size()));
            } catch (IllegalArgumentException ignored) {
                if (chasing) chasing = false;
                return;
            }
            int destRow = destination.getRow(),
                    destCol = destination.getColumn();
            if (destCol > currentBlock.getColumn()) {
                movingRight = true;
            } else if (destCol < currentBlock.getColumn()) {
                movingLeft = true;
            } else if (destRow > currentBlock.getRow()) {
                movingDown = true;
            } else if (destRow < currentBlock.getRow()) {
                movingUp = true;
            }
        }

    }

    private void moveTillHasOption(Block[][] surroundings){
        boolean canMoveRight = this.canMoveRight(surroundings);
        boolean canMoveLeft = this.canMoveLeft(surroundings);
        boolean canMoveDown = this.canMoveDown(surroundings);
        boolean canMoveUp = this.canMoveUp(surroundings);

        boolean canGoRightBlock = canMoveRight && surroundings[1][2].isPassable(this);
        boolean canGoLeftBlock = canMoveLeft && surroundings[1][0].isPassable(this);
        boolean canGoDownBlock = canMoveDown && surroundings[2][1].isPassable(this);
        boolean canGoUpBlock = canMoveUp && surroundings[0][1].isPassable(this);

        boolean reachedDestination = currentBlock == destination && !getBounds().intersects(origin.getBounds());

        if (movingRight){
            //Check whether it can go right
            if (canMoveRight){
                //Check whether has another option OR is moving to a chosen option
                if ((!canGoUpBlock && !canGoDownBlock)  || !reachedDestination){
                    //If is moving to a chosen option
                    if (!reachedDestination){
                        x += velX;
                    } else { //If has reached a chosen option
                        x += velX;
                        origin = currentBlock;
                        destination = surroundings[1][2];
                    }
                }else {
                    stand(standTimeChoosing);
                }
            }
            else stand(standTimeBlocked);
        }
        if (movingLeft) {
            //Check whether it can go left
            if (canMoveLeft){
                //Check whether has another option OR is moving to a chosen option
                if ((!canGoUpBlock && !canGoDownBlock) || !reachedDestination) {
                    //If is moving to a chosen option
                    if (!reachedDestination) {
                        x -= velX;
                    } else {  //If has reached a chosen option
                        x -= velX;
                        origin = currentBlock;
                        destination = surroundings[1][0];
                    }
                }else {
                    stand(standTimeChoosing);
                }
            }
            else stand(standTimeBlocked);
        }
        if (movingDown){
            //Check whether it can go down
            if (canMoveDown){
                //Check whether has another option OR is moving to a chosen option
                if ((!canGoRightBlock && !canGoLeftBlock) || !reachedDestination) {
                    //If is moving to a chosen option
                    if (!reachedDestination) {
                        y += velY;
                    }else {  //If has reached a chosen option
                        y += velY;
                        origin = currentBlock;
                        destination = surroundings[2][1];
                    }
                }else {
                    stand(standTimeChoosing);
                }
            }
            else stand(standTimeBlocked);
        }
        if (movingUp){
            //Check whether it can go up
            if (canMoveUp){
                //Check whether has another option OR is moving to a chosen option
                if ((!canGoRightBlock && !canGoLeftBlock) || !reachedDestination) {
                    //If is moving to a chosen option
                    if (!reachedDestination) {
                        y -= velY;
                    }else {  //If has reached a chosen option
                        y -= velY;
                        origin = currentBlock;
                        destination = surroundings[0][1];
                    }
                }else {
                    stand(standTimeChoosing);
                }
            }
            else stand(standTimeBlocked);
        }
    }

    private void stand(int standTime){
        faceDown = faceRight = faceLeft = faceUp = false;
//        if (standTime != 0){
        if (movingRight) faceRight = true;
        if (movingLeft) faceLeft = true;
        if (movingUp) faceUp = true;
        if (movingDown) faceDown = true;
//        }
        movingDown = movingRight = movingLeft = movingUp = false;
        standCounter = standTime;
    }

    @Override
    protected void load() {
        setUpAnimation("enemyLvl3");
    }
}
