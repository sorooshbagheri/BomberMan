package bomberMan.game.gameObjects;

import bomberMan.game.Renderable;
import bomberMan.game.Tickable;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public abstract class GameObject implements Tickable, Renderable, Serializable {

    protected double x, y;
    protected ObjectID id;
    protected Block currentBlock;
    protected double velX;
    protected double velY;

    protected boolean dying = false;
    protected boolean rotted = false;
    protected int rottingRate;

    protected int animIterator = 0;
    protected int dyingIterator = 0;
    protected int rottingIterator = 0;
    protected double animationSpeed;

    @Override
    public void render(Graphics g) {

    }



    public Block getBlock(GameBoard gameBoard){
        Rectangle2D r = (Rectangle2D) getBounds();
        return gameBoard.getBlock(r.getCenterX(),r.getCenterY());
    }

    public void setCurrentBlock(Block currentBlock) {
        this.currentBlock = currentBlock;
    }

    public abstract Shape getBounds();

    protected boolean canMoveRight(Block[][] surroundings){
        Shape futureBoundRight = getFutureBounds(velX,0);
        return (surroundings[0][2].isPassable(this) || !futureBoundRight.intersects(surroundings[0][2].getBounds()))
                && (surroundings[1][2].isPassable(this) || !futureBoundRight.intersects(surroundings[1][2].getBounds()))
                && (surroundings[2][2].isPassable(this) || !futureBoundRight.intersects(surroundings[2][2].getBounds()));
    }

    protected boolean canMoveLeft(Block[][] surroundings){
        Shape futureBoundLeft = getFutureBounds(-1*velX,0) ;
        return (surroundings[0][0].isPassable(this) || !futureBoundLeft.intersects(surroundings[0][0].getBounds()))
                && (surroundings[1][0].isPassable(this) || !futureBoundLeft.intersects(surroundings[1][0].getBounds()))
                && (surroundings[2][0].isPassable(this) || !futureBoundLeft.intersects(surroundings[2][0].getBounds()));
    }

    protected boolean canMoveDown(Block[][] surroundings){
        Shape futureBoundDown = getFutureBounds(0,velY) ;
        return (surroundings[2][0].isPassable(this) || !futureBoundDown.intersects(surroundings[2][0].getBounds()))
                && (surroundings[2][1].isPassable(this) || !futureBoundDown.intersects(surroundings[2][1].getBounds()))
                && (surroundings[2][2].isPassable(this) || !futureBoundDown.intersects(surroundings[2][2].getBounds()));
    }

    protected boolean canMoveUp(Block[][] surroundings){
        Shape futureBoundUp = getFutureBounds(0,-1*velY) ;
        return (surroundings[0][0].isPassable(this) || !futureBoundUp.intersects(surroundings[0][0].getBounds()))
                && (surroundings[0][1].isPassable(this) || !futureBoundUp.intersects(surroundings[0][1].getBounds()))
                && (surroundings[0][2].isPassable(this) || !futureBoundUp.intersects(surroundings[0][2].getBounds()));
    }

    protected abstract Shape getFutureBounds(double xIncrement, double yIncrement);

    public ObjectID getID() {
        return id;
    }

    protected void die(){
        dying = true;
    }

    protected void load(){

    }
}
