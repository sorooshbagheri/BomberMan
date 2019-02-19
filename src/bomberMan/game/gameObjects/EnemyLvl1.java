package bomberMan.game.gameObjects;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
public class EnemyLvl1 extends Enemy implements Serializable {

    private final int standTimeRandom = 5;
    private final int randomStandRate = 80;

    private int standCounter;
    private final int standTimeBlocked = 10;

    //move ver 2
    private final int standTimeChoosing = 0;
    private Block origin;
    private Block destination;

    public EnemyLvl1(GameBoard gameBoard) {
        super(gameBoard);
        id = ObjectID.Enemylvl1;
        width = (int) (0.6*Block.getHeight());
        height = (int) (0.9*Block.getHeight());
        velX = defaultVelocity;
        velY = velX;
        animationSpeed = 0.1*velX;
        setUpAnimation("enemyLvl1");
    }

    @Override
    public void tick() {
        if (!dying) {
            moveVer2();
        } else {
            dyingIterator++;
        }
    }

    @Override
    public void render(Graphics g) {
        if (!rotted) {
            renderPics(g);
            g.setColor(Color.BLUE);
        }
        g.drawRect((int)(x+Player.renderOffsetX),(int)(y+Player.renderOffsetY), width,height);
    }

    /**
     * The moving algorithm of this enemy is described in the following steps:
     *  1. Upon spawning chooses a random direction.
     *  2. Moves in that direction till stopped by a block OR it randomly stands.
     *  3. Chooses a random direction among non-blocked directions. If non existed, it just stands there.
     *  4. Goes to 2
     */
    @Override
    protected void move() {
        if (standCounter > 0){
            standCounter--;
            return;
        }

        Block[][] surroundings = gameBoard.getSurroundings(currentBlock.getRow(), currentBlock.getColumn(),1);
        if (movingUp || movingDown || movingLeft || movingRight) {
            if (random.nextInt(randomStandRate) == 1) {
                stand(standTimeRandom);
                return;
            }
            faceUp = faceLeft = faceRight = faceDown = false;
            moveTillBlocked(surroundings);
            animIterator++;
        }else {

            List<Block> freeSides = new ArrayList<>();
            if (surroundings[0][1].isPassable(this) && canMoveUp(surroundings)) freeSides.add(surroundings[0][1]);
            if (surroundings[1][0].isPassable(this) && canMoveLeft(surroundings)) freeSides.add(surroundings[1][0]);
            if (surroundings[1][2].isPassable(this) && canMoveRight(surroundings)) freeSides.add(surroundings[1][2]);
            if (surroundings[2][1].isPassable(this) && canMoveDown(surroundings)) freeSides.add(surroundings[2][1]);

            Block dest;
            try {
                dest = freeSides.get(random.nextInt(freeSides.size()));
            } catch (IllegalArgumentException ignored) {
                return;
            }
            int destRow = dest.getRow(),
                    destCol = dest.getColumn();
            if (destCol > currentBlock.getColumn()){
                movingRight = true;
            } else if (destCol < currentBlock.getColumn()){
                movingLeft = true;
            } else if (destRow > currentBlock.getRow()){
                movingDown = true;
            } else if (destRow < currentBlock.getRow()){
                movingUp = true;
            }
        }
    }

    /**
     * The second version of moving algorithm of this enemy is described in the following steps:
     *  1. Upon spawning chooses a random direction among the non-blocked ones.
     *  2. Moves in that direction till has another option.
     *  3. Chooses a random direction among non-blocked directions. If non existed, it just stands there.
     *  4. Goes to 2
     */
    private void moveVer2() {

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

            //Finding non-blocked sides
            List<Block> freeSides = new ArrayList<>();
            if (surroundings[0][1].isPassable(this) && canMoveUp(surroundings)) freeSides.add(surroundings[0][1]);
            if (surroundings[1][0].isPassable(this) && canMoveLeft(surroundings)) freeSides.add(surroundings[1][0]);
            if (surroundings[1][2].isPassable(this) && canMoveRight(surroundings)) freeSides.add(surroundings[1][2]);
            if (surroundings[2][1].isPassable(this) && canMoveDown(surroundings)) freeSides.add(surroundings[2][1]);

            origin = currentBlock;
            //Choosing a non-blocked side randomly
            try {
                destination = freeSides.get(random.nextInt(freeSides.size()));
            } catch (IllegalArgumentException ignored) {
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

    private void moveTillBlocked(Block[][] surroundings){
        if (movingRight){
            //Check whether it can go right
            if (this.canMoveRight(surroundings)){
                x += velX;
            }
            else stand(standTimeBlocked);
        }
        if (movingLeft) {
            //Check whether it can go left
            if (canMoveLeft(surroundings)){
                x -= velX;
            }
            else stand(standTimeBlocked);
        }
        if (movingDown){
            //Check whether it can go down
            if (canMoveDown(surroundings)){
                y += velY;
            }
            else stand(standTimeBlocked);
        }
        if (movingUp){
            //Check whether it can go up
            if (canMoveUp(surroundings)){
                y -= velY;
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
        setUpAnimation("enemyLvl1");
    }

}
