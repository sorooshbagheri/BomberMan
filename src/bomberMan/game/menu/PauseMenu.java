package bomberMan.game.menu;

import bomberMan.game.Game;
import bomberMan.game.Renderable;
import bomberMan.game.Tickable;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class PauseMenu implements Renderable,Tickable {

    private Game game;
    private LinkedList<MenuButton> buttons = new LinkedList<>();
    private MenuButton newGame, resume, saveAndQuit;

    int colorIterator;
    private Color color = Color.WHITE;


    public PauseMenu(Game game) {
        this.game = game;
        int h = Game.getGameHeight();

        newGame = new MenuButton("New Game",0.04,2.2,(int)(0.3*h));
        buttons.add(newGame);

        resume = new MenuButton("Resume",0.04,2.0,(int)(0.6*h));
        buttons.add(resume);

        saveAndQuit = new MenuButton("Quit",0.04,2.5,(int)(0.9*h));
        buttons.add(saveAndQuit);

        resume.select();
    }

    @Override
    public void render(Graphics g) {
        for (MenuButton button : buttons) {
            g.setColor(color);
            button.render(g);
        }
    }

    @Override
    public void tick() {
//        if (colorIterator > 0) {
            color = color == Color.WHITE ? Color.BLACK : Color.WHITE;
//            colorIterator = 0;
//        }else {
//            colorIterator++;
//        }
    }

    @SuppressWarnings("Duplicates")
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            //Moving between buttons
            case KeyEvent.VK_DOWN :
                for (int i = 0; i < buttons.size(); i++) {
                    MenuButton menuButton = buttons.get(i);
                    if (menuButton.isSelected()) {
                        try {
                            buttons.get(i + 1).select();
                        } catch (IndexOutOfBoundsException ex) {
                            buttons.getFirst().select();
                        }
                        menuButton.deselect();
                        return;
                    }
                } break;
            case KeyEvent.VK_UP :
                for (int i = 0; i < buttons.size(); i++) {
                    MenuButton menuButton = buttons.get(i);
                    if (menuButton.isSelected()) {
                        try {
                            buttons.get(i - 1).select();
                        } catch (IndexOutOfBoundsException ex) {
                            buttons.getLast().select();
                        }
                        menuButton.deselect();
                        return;
                    }
                } break;
            //Choosing a button
            case KeyEvent.VK_ENTER:
                for (MenuButton menuButton : buttons){
                    if (menuButton.isSelected()){
                        if (menuButton.getCaption().equals("Quit")){
                            game.setGameState(Game.GameState.inMenu);
                            game.save();
                            return;
                        }
                        if (menuButton.getCaption().equals("New Game")){
                            game.newGame();
                            return;
                        }
                        if (menuButton.getCaption().equals("Resume")){
                            game.setGameState(Game.GameState.inLevel);
                        }
                    }
                } break;
            default:
        }
    }
}
