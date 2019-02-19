package bomberMan.game.gameObjects;

import bomberMan.game.User;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public class Token extends GameObject implements Serializable {

    private int width, height;
    private TokenType tokenType;
    private GameBoard gameBoard;

    @Override
    public void tick() {

    }

    public enum TokenType {
        IncreaseBombsLimit,
        IncreaseBombRadius,
        IncreaseSpeed,
        IncreaseScore,
        DecreaseBombsLimit,
        DecreaseBombRadius,
        DecreaseSpeed,
        DecreaseScore,
        GhostMode,
        BombControl,
        Door
    }

    public Token(TokenType tokenType, Block block,GameBoard gameBoard) {
        this.tokenType = tokenType;
        currentBlock = block;
        this.gameBoard = gameBoard;
        height = width = Block.getHeight();
        x = block.x;
        y = block.y;
        id = ObjectID.Bomb;

    }

    @Override
    public void render(Graphics g) {
        if (currentBlock.id != ObjectID.Wall && !rotted){
            switch (tokenType){
                case Door:
                    g.setColor(new Color(0x633107));
                    g.fillRect((int)(x + Player.renderOffsetX),(int)(y + Player.renderOffsetY),(int)(1.0*width),(int)(1.0*height));
                    g.setColor(Color.BLACK);
                    g.drawString(tokenType.name(),(int)(x - 0.1*width + Player.renderOffsetX),(int)(y + 0.5*height + Player.renderOffsetY));
                    break;
                default:
                    g.setColor(Color.yellow);
                    g.fillOval((int)(x + 0.1*width + Player.renderOffsetX),(int)(y + 0.1*height + Player.renderOffsetY),(int)(0.8*width),(int)(0.8*height));
                    g.setColor(Color.BLACK);
                    g.drawString(tokenType.name(),(int)(x - 0.1*width + Player.renderOffsetX),(int)(y + 0.5*height + Player.renderOffsetY));
                    break;
            }
        }
//        g.setColor(Color.BLUE);
//        g.drawRect((int)(x + 0.1*width + Player.renderOffsetX),(int)(y + 0.1*height + Player.renderOffsetY),(int)(0.8*width),(int)(0.8*height));
    }

    public void affect(User user){
        //called token must not be dying
        switch (tokenType){
            case IncreaseBombsLimit:
                user.increaseBombsLimit();
                break;
            case IncreaseBombRadius:
                user.increaseBombRadius(gameBoard);
                break;
            case IncreaseSpeed:
                user.increaseSpeed();
                break;
            case IncreaseScore:
                user.increaseScore(100);
                break;
            case DecreaseBombsLimit:
                user.decreaseBombsLimit();
                break;
            case DecreaseBombRadius:
                user.decreaseBombRadius();
                break;
            case DecreaseSpeed:
                user.decreaseSpeed();
                break;
            case DecreaseScore:
                user.decreaseScore(100);
                break;
            case GhostMode:
                user.setGhostMode(true);
                break;
            case BombControl:
                user.setBombControl(true);
                break;
            case Door:
                break;
        }
    }

    @Override
    public Shape getBounds() {
        return new Rectangle2D.Double(x + 0.1*width,y + 0.1*height,0.8*width,0.8*height);
    }

    @Override
    protected Shape getFutureBounds(double xIncrement, double yIncrement) {
        return getBounds();
    }

    @Override
    protected void die() {
        if (!currentBlock.hasToken()) {
            dying = true;
            if (tokenType != TokenType.Door) {
                rotted = true;
            }
        }
    }
}
