package bomberMan.game.gameObjects;

import bomberMan.game.Game;
import bomberMan.game.User;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.LinkedList;

public class Player extends GameObject implements Serializable {

    static double renderOffsetX = 0, renderOffsetY = 0;
    private boolean movingRight, movingLeft, movingDown, movingUp
            , faceUp, faceDown, faceRight, faceLeft;
    private double width, height;

    private User user;
    private GameBoard gameBoard;

    private transient BufferedImage spriteSheet;
    private transient LinkedList<Image> rightAnim;
    private transient LinkedList<Image> leftAnim;
    private transient LinkedList<Image> upAnim;
    private transient LinkedList<Image> downAnim;
    private transient LinkedList<Image> dyingAnim;
    private transient Image rightStance, leftStance, upStance, downStance, dead;
    private int tileOffsetX, tileOffsetY;

    private LinkedList<Bomb> bombs = new LinkedList<>();

    public Player(User user, GameBoard gameBoard) {
        id = ObjectID.Player;
        this.user = user;
        this.gameBoard = gameBoard;
        width = (int) (0.8*Block.getHeight());
        height = (int) (0.8*Block.getHeight());
        velX = user.getSpeedLevel()*Enemy.defaultVelocity;
        velY = velX;
        animationSpeed = 0.1*velX;
        rottingRate = 50;
        faceRight = true;
        setUpAnimation();
       }

    @Override
    public void tick() {
        if (!dying) {
            move();
        }else {
            dyingIterator++;
        }

        //calculate rendering offset to keep a a proper view of the game board
        if (x + width/2 >= Game.getGameWidth()/2 && x +width/2 <= gameBoard.getWidth() - Game.getGameWidth()/2){
            renderOffsetX = Game.getGameWidth()/2 - (x + width/2);
        }
        if (y + height/2 >= Game.getGameHeight()/2 && y + height/2 <= gameBoard.getHeight() - Game.getGameHeight()/2){
            renderOffsetY = Game.getGameHeight()/2 - (y + height/2);
        }

        //get specs
        velY = velX = user.getSpeedLevel()*Enemy.defaultVelocity;
        animationSpeed = 0.1*velX;
    }

    @Override
    public void render(Graphics g) {
        int[] xs = {(int) (x+width/2.0 + renderOffsetX), (int) (x + renderOffsetX), (int) (x+width/2.0 + renderOffsetX),(int) (x+width + renderOffsetX)};
        int[] ys = {(int) (y + renderOffsetY), (int) (y+height/2.0 + renderOffsetY),(int) (y+height + renderOffsetY),(int) (y+height/2.0 + renderOffsetY)};

        renderPics(g);

        g.setColor(Color.BLUE);
        g.drawPolygon(xs,ys,4);

//        g.drawImage(rightStand,(int)(x+renderOffsetX+tileOffsetX),(int)(y+renderOffsetY+tileOffsetY),null);
//        g.drawRect((int)(x+renderOffsetX+tileOffsetX),(int)(y+renderOffsetY+tileOffsetY), rightStand.getWidth(null), rightStand.getHeight(null));

    }

    @SuppressWarnings("Duplicates")
    private void renderPics(Graphics g) {
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
        g.drawImage(next, (int) (x + renderOffsetX + tileOffsetX), (int) (y + renderOffsetY + tileOffsetY), null);

    }

    @Override
    public Shape getBounds() {
        int[] xs = {(int) (x+width/2.0), (int) (x), (int) (x+width/2.0),(int) (x+width)};
        int[] ys = {(int) (y), (int) (y+height/2.0),(int) (y+height),(int) (y+height/2.0)};
        return new Polygon(xs,ys,4);
    }

    protected Polygon getFutureBounds(double xIncrement, double yIncrement){
        int[] xs = {(int) (x+width/2.0 + xIncrement), (int) (x + xIncrement), (int) (x+width/2.0 + xIncrement),(int) (x+width + xIncrement)};
        int[] ys = {(int) (y + yIncrement), (int) (y+height/2.0 + yIncrement),(int) (y+height + yIncrement),(int) (y+height/2.0 + yIncrement)};
        return new Polygon(xs,ys,4);
    }

    @Override
    public Block getBlock(GameBoard gameBoard) {
        return gameBoard.getBlock(x+width/2.0,y+width/2);
    }

    public void setLocation(Block block) {
        x = block.x + 1;
        y = block.y + 1;
    }

    private void move(){
        Block[][] surroundings = gameBoard.getSurroundings(currentBlock.getRow(),currentBlock.getColumn(),1);

        if (movingRight) {
            //Check whether it can go right
            if (canMoveRight(surroundings)){
                x += velX;
            } else {
                //Check whether it can go upright
                Polygon futureBoundUpright = getFutureBounds(velX,-1*velY) ;
                if ((surroundings[0][2].isPassable(this) || !futureBoundUpright.intersects(surroundings[0][2].getBounds()))
                        && (surroundings[1][2].isPassable(this) || !futureBoundUpright.intersects(surroundings[1][2].getBounds()))){
                    x +=velX;
                    y -= velY;
                }
                //Check whether it can go downright
                Polygon futureBoundDownright = getFutureBounds(velX,velY) ;
                if ((surroundings[1][2].isPassable(this) || !futureBoundDownright.intersects(surroundings[1][2].getBounds()))
                        && (surroundings[2][2].isPassable(this) || !futureBoundDownright.intersects(surroundings[2][2].getBounds()))){
                    x +=velX;
                    y += velY;
                }
            }
        }
        if (movingLeft) {
            //Check whether it can go left
            if (canMoveLeft(surroundings)){
                x -= velX;
            } else {
                //Check whether it can go upleft
                Polygon futureBoundUpleft = getFutureBounds(-1*velX,-1*velY) ;
                if ((surroundings[0][0].isPassable(this) || !futureBoundUpleft.intersects(surroundings[0][0].getBounds()))
                        && (surroundings[1][0].isPassable(this) || !futureBoundUpleft.intersects(surroundings[1][0].getBounds()))){
                    x -=velX;
                    y -= velY;
                }
                //Check whether it can go downleft
                Polygon futureBoundDownleft = getFutureBounds(-1*velX,velY) ;
                if ((surroundings[1][0].isPassable(this) || !futureBoundDownleft.intersects(surroundings[1][0].getBounds()))
                        && (surroundings[2][0].isPassable(this) || !futureBoundDownleft.intersects(surroundings[2][0].getBounds()))){
                    x -=velX;
                    y += velY;
                }
            }

        }
        if (movingDown) {
            //Check whether it can go down
            if (canMoveDown(surroundings)){
                y += velY;
            } else {
                //Check whether it can go downright
                Polygon futureBoundDownright = getFutureBounds(velX,velY) ;
                if ((surroundings[2][1].isPassable(this) || !futureBoundDownright.intersects(surroundings[2][1].getBounds()))
                        && (surroundings[2][2].isPassable(this) || !futureBoundDownright.intersects(surroundings[2][2].getBounds()))){
                    x += velX;
                    y += velY;
                }
                //Check whether it can go downleft
                Polygon futureBoundDownleft = getFutureBounds(-1*velX,velY) ;
                if ((surroundings[2][0].isPassable(this) || !futureBoundDownleft.intersects(surroundings[2][0].getBounds()))
                        && (surroundings[2][1].isPassable(this) || !futureBoundDownleft.intersects(surroundings[2][1].getBounds()))){
                    x -=velX;
                    y += velY;
                }
            }
        }
        if (movingUp) {
            //Check whether it can go up
            if (canMoveUp(surroundings)){
                y -= velY;
            } else {
                //Check whether it can go upright
                Polygon futureBoundUpright = getFutureBounds(velX,-1*velY) ;
                if ((surroundings[0][1].isPassable(this) || !futureBoundUpright.intersects(surroundings[0][1].getBounds()))
                        && (surroundings[0][2].isPassable(this) || !futureBoundUpright.intersects(surroundings[0][2].getBounds()))){
                    x += velX;
                    y -= velY;
                }
                //Check whether it can go upleft
                Polygon futureBoundUpleft = getFutureBounds(-1*velX,-1*velY) ;
                if ((surroundings[0][0].isPassable(this) || !futureBoundUpleft.intersects(surroundings[0][0].getBounds()))
                        && (surroundings[0][1].isPassable(this) || !futureBoundUpleft.intersects(surroundings[0][1].getBounds()))){
                    x -=velX;
                    y -= velY;
                }
            }

        }
        if (movingRight || movingUp || movingDown || movingLeft){
            animIterator++;
            faceUp = faceLeft = faceRight = faceDown = false;
        }
    }

    public void keyPressed(KeyEvent e, Game game) {
        int key = e.getKeyCode();
        switch (key){
            case KeyEvent.VK_RIGHT:
                movingRight = true;
                break;
            case KeyEvent.VK_LEFT:
                movingLeft = true;
                break;
            case KeyEvent.VK_DOWN:
                movingDown = true;
                break;
            case KeyEvent.VK_UP:
                movingUp = true;
                break;
            case KeyEvent.VK_B:
                putBomb();
                break;
            case KeyEvent.VK_SPACE:
                if (user.hasBombControl()){
                    for (Bomb bomb : bombs) {
                        if (!bomb.dying){
                            bomb.die();
                            break;
                        }
                    }
                }
                break;
            case KeyEvent.VK_ESCAPE:
                game.setGameState(Game.GameState.PauseMenu);

        }
    }

    private void putBomb() {
        if (gameBoard.getTotalBombs(this) < user.getBombLimit()
                && !currentBlock.hasBomb()
                && currentBlock.id == ObjectID.Passage){
            Bomb bomb = new Bomb(gameBoard,this,user.getBombRadius(),user.hasBombControl());
            gameBoard.putBomb(bomb);
            if (user.hasBombControl()){
                bombs.add(bomb);
            }
        }
    }

    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();
        switch (key){
            case KeyEvent.VK_RIGHT:
                movingRight = false;
                faceRight = true;
                break;
            case KeyEvent.VK_LEFT:
                movingLeft = false;
                faceLeft = true;
                break;
            case KeyEvent.VK_DOWN:
                movingDown = false;
                faceDown = true;
                break;
            case KeyEvent.VK_UP:
                movingUp = false;
                faceUp = true;
                break;
        }

    }

    public User getUser() {
        return user;
    }

    public static void resetRenderOffset(){
        renderOffsetX = 0;
        renderOffsetY = 0;
    }

    public void load() {
        setUpAnimation();
    }

    private void setUpAnimation() {
        rightAnim = new LinkedList<>();
        leftAnim = new LinkedList<>();
        upAnim = new LinkedList<>();
        downAnim = new LinkedList<>();
        dyingAnim = new LinkedList<>();

        //Loading animations : Getting the sprite sheet
        try {
            spriteSheet = Game.imageLoader.getSheet("player");
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
}
