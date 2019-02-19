package bomberMan;

import bomberMan.game.Game;
import bomberMan.game.gameObjects.Player;
import bomberMan.game.menu.Menu;
import bomberMan.game.menu.PauseMenu;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {

    private Game game;
    private Menu menu;
    private Player player;
    private PauseMenu pauseMenu;

    public KeyInput(Game game, Menu menu, PauseMenu pauseMenu) {
        this.game = game;
        this.menu = menu;
        this.pauseMenu = pauseMenu;
    }

    @Override
    public void keyPressed(KeyEvent e) {
//        System.out.println(e);

        //Pausing the game
        if (e.getKeyCode() == KeyEvent.VK_P){
            if (game.isRunning()){
                game.pause();
            }else {
                game.start();
            }
        }

        if (game.isRunning()) {
            switch (game.getGameState()){
                case inMenu:
                    menu.keyPressed(e);
                    break;
                case inLevel:
                    player.keyPressed(e,game);
                    break;
                case PauseMenu:
                    pauseMenu.keyPressed(e);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        if (game.isRunning()) {
            switch (game.getGameState()) {
                case inMenu:
                    //do nothing
                    break;
                case inLevel:
                    player.keyReleased(e);
                    break;
            }
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
