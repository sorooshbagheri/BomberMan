package bomberMan.game.menu;

import bomberMan.ScreenInfo;
import bomberMan.game.Game;
import bomberMan.game.Renderable;
import bomberMan.game.Tickable;

import java.awt.*;

public class MenuButton implements Renderable, Tickable {

    private String caption;
    private double fractionOfHeight;
    private double widthToHeightRatio;
    private Font font;
    private Point location;
    private boolean selected;

    /**
     *  Creates a menu button.
     *  In order to center the button horizontally the x value of location must be half the game frame size.
     * @param caption Caption of the button
     * @param ratio1 ratio of the button's height to the game frame height
     * @param ratio2 ratio of the button's width to the button's height
     * @param yLocation
     */
    public MenuButton(String caption, double ratio1, double ratio2, int yLocation) {
        this.caption = caption;
        this.fractionOfHeight = ratio1;
        this.widthToHeightRatio = ratio2;
        this.location = new Point((int) (0.5* Game.getGameWidth()),yLocation);

    }

    @Override
    public void render(Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        double fontSize = fractionOfHeight * Game.getGameHeight() * ScreenInfo.getScreenResolution() / 72.0; //pixels * dot/inch / point/inch ??
        if (!selected) {
            g.setColor(Color.WHITE);
        }
        g.setFont(new Font(null,Font.ITALIC, (int) fontSize));
        g.drawString(caption, (int) (location.x - (widthToHeightRatio*fm.getHeight())/2), location.y);

    }

    @Override
    public void tick() {

    }

    void select(){
        selected = true;
    }

    void deselect(){
        selected= false;
    }

    public boolean isSelected() {
        return selected;
    }

    public String getCaption() {
        return caption;
    }
}
