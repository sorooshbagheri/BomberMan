package bomberMan.game.gameObjects;

import bomberMan.game.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Random;

public abstract class Enemy extends GameObject implements Serializable {

    protected boolean movingRight, movingLeft, movingDown, movingUp
            , faceUp, faceDown, faceRight, faceLeft;
    protected int width, height;
    protected static double defaultVelocity;


    protected GameBoard gameBoard;

    protected Random random = new Random();

    private transient BufferedImage spriteSheet;
    private transient LinkedList<Image> rightAnim;
    private transient LinkedList<Image> leftAnim;
    private transient LinkedList<Image> upAnim;
    private transient LinkedList<Image> downAnim ;
    private transient LinkedList<Image> dyingAnim;
    private transient Image rightStance, leftStance, upStance, downStance, dead;
    private int tileOffsetX, tileOffsetY;

    public Enemy(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        rottingRate = 50;
        Enemy.defaultVelocity = 1.0D*Block.getHeight()/60.0D;
    }

    protected void setUpAnimation(String spriteSheetName){
        switch (random.nextInt(4)){
            case 0 : faceUp = true; break;
            case 1 : faceDown = true; break;
            case 2 : faceRight = true; break;
            case 3 : faceLeft = true; break;
        }

        rightAnim = new LinkedList<>();
        leftAnim = new LinkedList<>();
        upAnim = new LinkedList<>();
        downAnim = new LinkedList<>();
        dyingAnim = new LinkedList<>();

        //Setting up animations
        //Getting the sprite sheet
        try {
            spriteSheet = Game.imageLoader.getSheet(spriteSheetName);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        //Calculating cropping and scaling and offset information
        int tileW = spriteSheet.getWidth() / 13;
        int tileH = spriteSheet.getHeight() / 21;
        int netH = 47;
        int netW = 30;
        double tileScaleW = (1.2 * width/ netW);
        double tileScaleH =  (1.0* height/ netH);
        tileOffsetX = (int) ((-1)*(tileScaleW* tileW /2-width/2));
        tileOffsetY = (int) ((-1)*(tileScaleH*(tileH - 2 - netH /2) - height/2));

        //Preparing right animations
        for (int i = 1; i <= 8; i++) {
            rightAnim.add(spriteSheet.getSubimage(tileW*i,tileH*11,tileW,tileH).getScaledInstance((int)(tileScaleW* tileW),(int)(tileScaleH* tileH),Image.SCALE_DEFAULT));
        }
        rightStance = spriteSheet.getSubimage(tileW*0,tileH*11,tileW,tileH).getScaledInstance((int)(tileScaleW* tileW),(int)(tileScaleH* tileH),Image.SCALE_DEFAULT);
        //Preparing down animations
        for (int i = 1; i <= 8; i++) {
            downAnim.add(spriteSheet.getSubimage(tileW*i,tileH*10,tileW,tileH).getScaledInstance((int)(tileScaleW* tileW),(int)(tileScaleH* tileH),Image.SCALE_DEFAULT));
        }
        downStance = spriteSheet.getSubimage(tileW*0,tileH*10,tileW,tileH).getScaledInstance((int)(tileScaleW* tileW),(int)(tileScaleH* tileH),Image.SCALE_DEFAULT);
        //Preparing left animations
        for (int i = 1; i <= 8; i++) {
            leftAnim.add(spriteSheet.getSubimage(tileW*i,tileH*9,tileW,tileH).getScaledInstance((int)(tileScaleW* tileW),(int)(tileScaleH* tileH),Image.SCALE_DEFAULT));
        }
        leftStance = spriteSheet.getSubimage(tileW*0,tileH*9,tileW,tileH).getScaledInstance((int)(tileScaleW* tileW),(int)(tileScaleH* tileH),Image.SCALE_DEFAULT);
        //Preparing up animations
        for (int i = 1; i <= 8; i++) {
            upAnim.add(spriteSheet.getSubimage(tileW*i,tileH*8,tileW,tileH).getScaledInstance((int)(tileScaleW* tileW),(int)(tileScaleH* tileH),Image.SCALE_DEFAULT));
        }
        upStance = spriteSheet.getSubimage(tileW*0,tileH*8,tileW,tileH).getScaledInstance((int)(tileScaleW* tileW),(int)(tileScaleH* tileH),Image.SCALE_DEFAULT);
        //Preparing dying animations
        for (int i = 0; i <= 5; i++) {
            dyingAnim.add(spriteSheet.getSubimage(tileW*i,tileH*20,tileW,tileH).getScaledInstance((int)(tileScaleW* tileW),(int)(tileScaleH* tileH),Image.SCALE_DEFAULT));
        }
        dead = spriteSheet.getSubimage(tileW*5,tileH*20,tileW,tileH).getScaledInstance((int)(tileScaleW* tileW),(int)(tileScaleH* tileH),Image.SCALE_DEFAULT);
    }

    @SuppressWarnings("Duplicates")
    protected void renderPics(Graphics g){
        Image next = null;
        if (movingDown){
            try {
                next = downAnim.get((int) Math.floor(animationSpeed* animIterator));
            } catch (Exception e) {
                animIterator = 0;
                next = downAnim.get(animIterator);
            }
        }
        if (movingRight){
            try {
                next = rightAnim.get((int) Math.floor(animationSpeed* animIterator));
            } catch (Exception e) {
                animIterator = 0;
                next = rightAnim.get(animIterator);
            }
        }
        if (movingLeft){
            try {
                next = leftAnim.get((int) Math.floor(animationSpeed* animIterator));
            } catch (Exception e) {
                animIterator = 0;
                next = leftAnim.get(animIterator);
            }
        }
        if (movingUp) {
            try {
                next = upAnim.get((int) Math.floor(animationSpeed * animIterator));
            } catch (Exception e) {
                animIterator = 0;
                next = upAnim.get(animIterator);
            }
        }
        if (faceRight) next = rightStance;
        if (faceLeft) next = leftStance;
        if (faceDown) next = downStance;
        if (faceUp) next = upStance;

        if (dying){
            try {
                next = dyingAnim.get((int) Math.floor(0.1*0.5*Block.getHeight()/30 * dyingIterator));
            }catch (Exception e){
                next = dead;
                if (rottingIterator > rottingRate) rotted = true;
                else rottingIterator++;
                dyingIterator++;
            }
        }

        g.drawImage(next, (int) (x + Player.renderOffsetX + tileOffsetX), (int) (y + Player.renderOffsetY + tileOffsetY), null);

    }

    @Override
    public Rectangle2D.Double getBounds() {
        return new Rectangle2D.Double(x,y,width,height);
    }

    protected Rectangle2D getFutureBounds(double xIncrement, double yIncrement){
        return new Rectangle2D.Double(x + xIncrement,y + yIncrement, width,height);
    }

    public void setLocation(Block block) {
        x = block.x + 1;
        y = block.y + 1;
    }

    protected abstract void move();

}
