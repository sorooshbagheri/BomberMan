package bomberMan.game.menu;

import bomberMan.ScreenInfo;
import bomberMan.game.Game;
import bomberMan.game.Renderable;
import bomberMan.game.Tickable;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

public class Menu implements Renderable, Tickable {

    public static Game game; //todo private non-static
    private final String title = "BomberMan!"; //Size and location customization of the font is needed in case of changing the title
    private LinkedList<MenuButton> menuButtons = new LinkedList<>();
    private MenuButton start, load, extra, options, exit;
    private MenuState menuState = MenuState.Main;
    private Random random = new Random();
    private boolean ascendR, ascendG, ascendB;
    private int r = random.nextInt(255),
            g = random.nextInt(255),
            b = random.nextInt(255);
    private int rVel = 15, gVel = 10, bVel = 5;

    public enum MenuState{
        Main,
        Extera,
        Options
    }

    public Menu(Game game) {
        this.game = game;
        int h = Game.getGameHeight();

        start = new MenuButton("NEW GAME",0.05, 2.2, (int) (0.5*h));
        menuButtons.add(start);
        load = new MenuButton("CONTINUE", 0.05, 4.2, (int) (0.65*h));
        menuButtons.add(load);
        exit = new MenuButton("EXIT",0.05, 1.8, (int) (0.8*h));
        menuButtons.add(exit);

        start.select();
    }

    @Override
    public void render(Graphics g) {
        //Title
        int h = Game.getGameHeight();
        int w = Game.getGameWidth();
        double fontSize= 0.1 * Game.getGameHeight() * ScreenInfo.getScreenResolution() / 72.0; //pixels * dot/inch / point/inch ??
        FontMetrics fm;

        g.setColor(Color.RED);
        g.setFont(new Font(null,Font.BOLD|Font.ITALIC, (int) fontSize+2));
        fm = g.getFontMetrics();
        g.drawString(title, (int) (0.5*w-(6.2*fm.getHeight())/2), (int) (0.2*h));

        g.setColor(Color.WHITE);
        g.setFont(new Font(null,Font.BOLD|Font.ITALIC, (int) fontSize));
        fm = g.getFontMetrics();
        g.drawString(title, (int) (0.5*w-(6.2*fm.getHeight())/2), (int) (0.2*h));

        //Menu Buttons
        for (MenuButton menuButton : menuButtons){
            g.setColor(new Color(this.r,this.g,this.b));
            menuButton.render(g);
        }

    }

    @Override
    public void tick() {
        changeColor();
    }

    @SuppressWarnings("Duplicates")
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            //Moving between buttons
            case KeyEvent.VK_DOWN :
                for (int i = 0; i < menuButtons.size(); i++) {
                    MenuButton menuButton = menuButtons.get(i);
                    if (menuButton.isSelected()) {
                        try {
                            menuButtons.get(i + 1).select();
                        } catch (IndexOutOfBoundsException ex) {
                            menuButtons.getFirst().select();
                        }
                        menuButton.deselect();
                        return;
                    }
                } break;
            case KeyEvent.VK_UP :
                for (int i = 0; i < menuButtons.size(); i++) {
                    MenuButton menuButton = menuButtons.get(i);
                    if (menuButton.isSelected()) {
                        try {
                            menuButtons.get(i - 1).select();
                        } catch (IndexOutOfBoundsException ex) {
                            menuButtons.getLast().select();
                        }
                        menuButton.deselect();
                        return;
                    }
                } break;
            //Choosing a button
            case KeyEvent.VK_ENTER:
                for (MenuButton menuButton : menuButtons){
                    if (menuButton.isSelected()){
                        if (menuButton.getCaption().equals("EXIT")){
                            game.exit();
                            return;
                        }
                        if (menuButton.getCaption().equals("NEW GAME")){
                            game.newGame();
                            return;
                        }
                        if (menuButton.getCaption().equals("CONTINUE")){
                            game.load();
                            return;
                        }
                    }
                } break;
            default:
        }
    }

    public void setMenuState(MenuState menuState){
        this.menuState = menuState;
    }

    private void changeColor(){
        if (r - rVel < 0) ascendR = true;
        if (r + rVel > 255) ascendR = false;
        r = ascendR ? r+rVel : r -rVel;

        if (g - gVel < 0) ascendG = true;
        if (g + gVel > 255) ascendG = false;
        g = ascendG ? g+gVel : g-gVel;

        if (b - bVel < 0) ascendB = true;
        if (b + bVel > 255) ascendB = false;
        b = ascendB ? b+bVel : b-bVel;
    }


}
