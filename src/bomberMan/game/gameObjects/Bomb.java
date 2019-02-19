package bomberMan.game.gameObjects;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public class Bomb extends GameObject implements Serializable {

    private GameBoard gameBoard;
    private Player player;

    private int width, height;

    private int range;
    private boolean manual;
    private final static int cookingTime = 300;
    private int explodeIterator;

    private final static int spreadSpeed = 8; //frames

    public Bomb(GameBoard gameBoard, Player player, int range, boolean manual) {
        this.gameBoard = gameBoard;
        this.player = player;
        this.range = range;
        this.manual = manual;
        this.explodeIterator = cookingTime;
        id = ObjectID.Bomb;
        height = width = Block.getHeight();
        currentBlock = player.currentBlock;
        x = player.currentBlock.x;
        y = player.currentBlock.y;
    }

    @Override
    public void render(Graphics g) {
        if (!rotted) {
            g.setColor(Color.BLACK);
            g.fillOval((int)(x + Player.renderOffsetX),(int)(y+Player.renderOffsetY),width, height);
        }
    }

    @Override
    public void tick() {
        if (!manual && !dying){
            if (explodeIterator == 0){
                die();
            }else {
                explodeIterator--;
            }
        }
    }

    @Override
    public Shape getBounds() {
        return new Rectangle2D.Double(x,y,width,height);
    }

    @Override
    protected Shape getFutureBounds(double xIncrement, double yIncrement) {
        return getBounds();
    }

    @SuppressWarnings("Duplicates")
    @Override
    protected void die(){
        if (!rotted) {
            dying = true;
            dyingIterator++;
            currentBlock.setHasBomb(false);

            rotted = true;

            Block[][] surroundings = gameBoard.getSurroundings(currentBlock.getRow(), currentBlock.getColumn(), range);
            surroundings[range][range].explode(0*spreadSpeed);
            //Up
            for (int i = 0; i < range; i++) {
                Block block = surroundings[range - i - 1][range];
                try {
                    if (block.id == ObjectID.Passage) {
                        block.explode((i+1)*spreadSpeed);
                    }else if (block.id == ObjectID.HardBlock) {
                        break;
                    } else if (block.id == ObjectID.Wall) {
                        block.explode((i+1)*spreadSpeed);
                        break;
                    }
                }catch (NullPointerException ignored){
                    //trying to access out of border blocks
                }
            }
            //Left
            for (int i = 0; i < range; i++) {
                Block block = surroundings[range][range - i - 1];
                try {
                    if (block.id == ObjectID.Passage) {
                        block.explode((i+1)*spreadSpeed);
                    }else if (block.id == ObjectID.HardBlock) {
                        break;
                    } else if (block.id == ObjectID.Wall) {
                        block.explode((i+1)*spreadSpeed);
                        break;
                    }
                }catch (NullPointerException ignored){
                    //trying to access out of border blocks
                }
            }
            //Down
            for (int i = 0; i < range; i++) {
                Block block = surroundings[range + i + 1][range];
                try {
                    if (block.id == ObjectID.Passage) {
                        block.explode((i+1)*spreadSpeed);
                    }else if (block.id == ObjectID.HardBlock) {
                        break;
                    } else if (block.id == ObjectID.Wall) {
                        block.explode((i+1)*spreadSpeed);
                        break;
                    }
                }catch (NullPointerException ignored){
                    //trying to access out of border blocks
                }
            }
            //Right
            for (int i = 0; i < range; i++) {
                Block block = surroundings[range][range + i + 1];
                try {
                    if (block.id == ObjectID.Passage) {
                        block.explode((i+1)*spreadSpeed);
                    }else if (block.id == ObjectID.HardBlock) {
                        break;
                    }else if (block.id == ObjectID.Wall) {
                        block.explode((i+1)*spreadSpeed);
                        break;
                    }
                }catch (NullPointerException ignored){
                    //trying to access out of border blocks
                }
            }
        }
    }

    public Player getPlayer() {
        return player;
    }
}
