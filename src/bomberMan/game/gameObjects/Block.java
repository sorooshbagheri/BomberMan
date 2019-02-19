package bomberMan.game.gameObjects;

import bomberMan.game.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;

public class Block extends GameObject implements Serializable {

    private int row, column;
    private static double blockFractionOfHeight;
    private static int height;
    private boolean hasBomb = false;
    private LinkedList<GameObject> containingObjects;
    private GameBoard gameBoard;

    private transient static Image hardBlockImg;
    private transient static Image wallImg;
    private transient static Image passageImg;

    private boolean exploComing = false;
    private int exploCountDown;

    private boolean hasToken = false;

    public Block(int x, int y, ObjectID id,int row,int column,GameBoard gameBoard) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.row = row;
        this.column = column;
        this.gameBoard = gameBoard;


        if (hardBlockImg == null || wallImg == null || passageImg == null) {
            try {
                hardBlockImg = ImageIO.read(getClass().getResource("../../res/3/set-of-pixel-art-gaming-background-icons_2022263_2.png")).getScaledInstance(Block.height,Block.height,Image.SCALE_DEFAULT);
                wallImg = ImageIO.read(getClass().getResource("../../res/3/set-of-pixel-art-gaming-background-icons_2022263_6.png")).getScaledInstance(Block.height,Block.height,Image.SCALE_DEFAULT);
                passageImg = ImageIO.read(getClass().getResource("../../res/3/set-of-pixel-art-gaming-background-icons_2022263_5.png")).getScaledInstance(Block.height,Block.height,Image.SCALE_DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void render(Graphics g) {
        int[]xs;
        int[]ys;
        switch (id){
            case BorderBlock:
//                //Upper triangle
//                g.setColor(new Color(0xE6E6E6));
//                xs = new int[]{(int) (x + Player.renderOffsetX), (int) (x + Player.renderOffsetX) + height, (int)(x+Player.renderOffsetX)};
//                ys = new int[]{(int)(y+Player.renderOffsetY),(int)(y+Player.renderOffsetY),(int)(y+Player.renderOffsetY)+height};
//                g.fillPolygon(xs,ys,3);
//
//                //Lower Triangle
//                g.setColor(new Color(0x494949));
//                xs = new int[]{(int) (x + Player.renderOffsetX)+height, (int) (x + Player.renderOffsetX), (int)(x+Player.renderOffsetX)+height};
//                ys = new int[]{(int)(y+Player.renderOffsetY),(int)(y+Player.renderOffsetY)+height,(int)(y+Player.renderOffsetY)+height};
//                g.fillPolygon(xs,ys,3);
//
//                //Covering rectangle
//                g.setColor(new Color(0xABABAB));
//                g.fillRect((int)(x+Player.renderOffsetX+0.1*height),(int)(y+Player.renderOffsetY+0.1*height),(int)(0.8*height),(int)(0.8*height));
                g.drawImage(hardBlockImg,(int)(x+Player.renderOffsetX),(int)(y+Player.renderOffsetY),null);
                break;
            case Passage:
                g.drawImage(passageImg,(int)(x+Player.renderOffsetX),(int)(y+Player.renderOffsetY),null);
                break;
            case HardBlock:
//                //Upper triangle
//                g.setColor(new Color(0xE6E6E6));
//                xs = new int[]{(int) (x + Player.renderOffsetX), (int) (x + Player.renderOffsetX) + height, (int)(x+Player.renderOffsetX)};
//                ys = new int[]{(int)(y+Player.renderOffsetY),(int)(y+Player.renderOffsetY),(int)(y+Player.renderOffsetY)+height};
//                g.fillPolygon(xs,ys,3);
//
//                //Lower Triangle
//                g.setColor(new Color(0x494949));
//                xs = new int[]{(int) (x + Player.renderOffsetX)+height, (int) (x + Player.renderOffsetX), (int)(x+Player.renderOffsetX)+height};
//                ys = new int[]{(int)(y+Player.renderOffsetY),(int)(y+Player.renderOffsetY)+height,(int)(y+Player.renderOffsetY)+height};
//                g.fillPolygon(xs,ys,3);
////
//                //Covering rectangle
//                g.setColor(new Color(0xABABAB));
//                g.fillRect((int)(x+Player.renderOffsetX+0.1*height),(int)(y+Player.renderOffsetY+0.1*height),(int)(0.8*height),(int)(0.8*height));

//                g.setColor(Color.BLACK);
//                g.drawRect((int)(x+Player.renderOffsetX),(int)(y+Player.renderOffsetY),(int)(height),(int)(height));
                g.drawImage(hardBlockImg,(int)(x+Player.renderOffsetX),(int)(y+Player.renderOffsetY),null);
                break;
            case Wall:
//                //Background
//                g.setColor(new Color(0x494949));
//                g.fillRect((int)(x+Player.renderOffsetX), (int)(y+Player.renderOffsetY),height,height);
//
//                g.setColor(new Color(0xE6E6E6));
//                xs = getWallTriangleXs(x + 5.0*height/6 + Player.renderOffsetX, 1.0*height/6.0);
//                ys = getWallTriangleYs(y + Player.renderOffsetY, 1.0*height/6.0);
//                g.fillPolygon(xs,ys,3);
//
//                xs = getWallTriangleXs(x + Player.renderOffsetX, 1.0*height/6.0);
//                ys = getWallTriangleYs(y + 1.0*height/6 + Player.renderOffsetY, 1.0*height/6.0);
//                g.fillPolygon(xs,ys,3);
//
//                xs = getWallTriangleXs(x + 1.0*height/6 + Player.renderOffsetX, 1.0*height/6.0);
//                ys = getWallTriangleYs(y + 2.0*height/6 + Player.renderOffsetY, 1.0*height/6.0);
//                g.fillPolygon(xs,ys,3);
//
//                xs = getWallTriangleXs(x + 2.0*height/6 + Player.renderOffsetX, 1.0*height/6.0);
//                ys = getWallTriangleYs(y + 3.0*height/6 + Player.renderOffsetY, 1.0*height/6.0);
//                g.fillPolygon(xs,ys,3);
//
//                xs = getWallTriangleXs(x + 3.0*height/6 + Player.renderOffsetX, 1.0*height/6.0);
//                ys = getWallTriangleYs(y + 4.0*height/6 + Player.renderOffsetY, 1.0*height/6.0);
//                g.fillPolygon(xs,ys,3);
//
//                xs = getWallTriangleXs(x + 4.0*height/6 + Player.renderOffsetX, 1.0*height/6.0);
//                ys = getWallTriangleYs(y + 5.0*height/6 + Player.renderOffsetY, 1.0*height/6.0);
//                g.fillPolygon(xs,ys,3);
//
//                g.fillRect((int)(x + 0.0*height/6.0 + Player.renderOffsetX), (int)(y + 0.0*height/6.0 + Player.renderOffsetY), (int)(5.0*height/6.0),(int)(1.0*height/6.0));
//                g.fillRect((int)(x + 0.0*height/6.0 + Player.renderOffsetX), (int)(y + 2.0*height/6.0 + Player.renderOffsetY), (int)(1.0*height/6.0),(int)(1.0*height/6.0));
//                g.fillRect((int)(x + 2.0*height/6.0 + Player.renderOffsetX), (int)(y + 2.0*height/6.0 + Player.renderOffsetY), (int)(4.0*height/6.0),(int)(1.0*height/6.0));
//                g.fillRect((int)(x + 0.0*height/6.0 + Player.renderOffsetX), (int)(y + 4.0*height/6.0 + Player.renderOffsetY), (int)(3.0*height/6.0),(int)(1.0*height/6.0));
//                g.fillRect((int)(x + 4.0*height/6.0 + Player.renderOffsetX), (int)(y + 4.0*height/6.0 + Player.renderOffsetY), (int)(2.0*height/6.0),(int)(1.0*height/6.0));
//
//                //Covering rectangles
//                g.setColor(new Color(0xABABAB));
//                g.fillRect((int)(x + 1.0*height/30.0 + Player.renderOffsetX), (int)(y + 1.0*height/30.0 + Player.renderOffsetY), (int)(28.0*height/30.0),(int)(8.0*height/30.0));
//                g.fillRect((int)(x + 0.0*height/30.0 + Player.renderOffsetX), (int)(y + 11.0*height/30.0 + Player.renderOffsetY), (int)(9.0*height/30.0),(int)(8.0*height/30.0));
//                g.fillRect((int)(x + 11.0*height/30.0 + Player.renderOffsetX), (int)(y + 11.0*height/30.0 + Player.renderOffsetY), (int)(19.0*height/30.0),(int)(8.0*height/30.0));
//                g.fillRect((int)(x + 0.0*height/30.0 + Player.renderOffsetX), (int)(y + 21.0*height/30.0 + Player.renderOffsetY), (int)(19.0*height/30.0),(int)(8.0*height/30.0));
//                g.fillRect((int)(x + 21.0*height/30.0 + Player.renderOffsetX), (int)(y + 21.0*height/30.0 + Player.renderOffsetY), (int)(9.0*height/30.0),(int)(8.0*height/30.0));
                g.drawImage(wallImg,(int)(x+Player.renderOffsetX),(int)(y+Player.renderOffsetY),null);
                break;
        }
        if (exploCountDown<=5 && exploCountDown >=1){
            g.setColor(Color.red);
            g.fillRect((int)(x+Player.renderOffsetX),(int)(y+Player.renderOffsetY),height,height);
        }
    }

    /**
     *  Gives the x coordinates of a triangle shaped like
     *   ___
     *  |  /
     *  | /
     *  |/
     *
     *  This method is used for rendering of each wall
     * @param x top left x coordinate
     * @param height height of triangle
     * @return int array of triangle xs
     */
    private int[] getWallTriangleXs(double x, double height){
        return new int[]{(int)(x),(int)(x+height),(int)(x)};
    }

    /**
     *  Gives the y coordinates of a triangle shaped like
     *   ___
     *  |  /
     *  | /
     *  |/
     *
     *  This method is used for rendering of each wall.
     * @param y top left y coordinate
     * @param height height of triangle
     * @return int array of triangle ys
     */
    private int[] getWallTriangleYs(double y, double height){
        return new int[]{(int)(y),(int)(y),(int)(y+height)};
    }

    public static int getHeight() {
        return height;
    }

    private static void setHeight() {
        Block.height = (int) (blockFractionOfHeight*Game.getGameHeight());
    }

    /**
     *  Sets ratio of each block's height to the game frame height and also sets the height of each block.
     * @param ratio
     */
    public static void setBlockFractionOfHeight(double ratio) {
        Block.blockFractionOfHeight = ratio;
        setHeight();
    }

    @Override
    public Rectangle2D.Double getBounds(){
        return new Rectangle2D.Double(x, y, height, height);
    }

    @Override
    protected Shape getFutureBounds(double xIncrement, double yIncrement) {
        return getBounds();
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isPassable(GameObject gameObject) {
        if (id == ObjectID.BorderBlock || hasBomb) return false;
        switch (gameObject.id){
            case Player:
                if (((Player) gameObject).getUser().isGhostMode()) return true;
                return id != ObjectID.Wall && id != ObjectID.HardBlock;
            case Enemylvl1:
                return id != ObjectID.Wall && id != ObjectID.HardBlock;
            case Enemylvl2:
                return id != ObjectID.Wall && id != ObjectID.HardBlock;
            case Enemylvl3:
                return id != ObjectID.Wall && id != ObjectID.HardBlock;
            case Enemylvl4:
                return true;
        }
            return true;
    }

    public void placeWall() {
        id = ObjectID.Wall;
    }

    @Override
    protected boolean canMoveRight(Block[][] surroundings) {
        return false;
    }

    @Override
    protected boolean canMoveLeft(Block[][] surroundings) {
        return false;
    }

    @Override
    protected boolean canMoveDown(Block[][] surroundings) {
        return false;
    }

    @Override
    protected boolean canMoveUp(Block[][] surroundings) {
        return false;
    }

    @Override
    public void tick() {
        if (exploComing) explode(-1);
    }

    public void explode(int spreadSpeed) {
        if (!exploComing){
            exploComing = true;
            exploCountDown = spreadSpeed;

        } else {
            if (exploCountDown == 0){
                for (GameObject gameObject : gameBoard.getGameObjects()) {
                    if (gameObject.getBounds().intersects(currentBlock.getBounds()) ||
                            gameObject.currentBlock == this) {
                        gameObject.die();
                    }
                }
                exploComing = false;
            }else exploCountDown--;
        }
    }

    public void setHasBomb(boolean hasBomb) {
        this.hasBomb = hasBomb;
    }

    @Override
    protected void die() {
        switch (id){
            case Wall: id = ObjectID.Passage; break;
            case Passage: hasToken = false; break;
        }
    }

    public boolean hasBomb() {
        return hasBomb;
    }

    public boolean hasToken() {
        return hasToken;
    }

    public void setHasToken(boolean hasToken) {
        this.hasToken = hasToken;
    }
}
