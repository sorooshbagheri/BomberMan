package bomberMan.tutorial;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {

    Handler handler;

    public KeyInput(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void keyPressed(KeyEvent e) {
//        System.out.println(e);
        int key = e.getKeyCode();
        for (GameObject gameObject : handler.gameObjects){
            if (gameObject.id.equals(ID.Player)){
                Player player = (Player) gameObject;
                switch (key){
                    case KeyEvent.VK_UP : {
                        player.goingUp = true;
                        return;
                    }
                    case KeyEvent.VK_DOWN : {
                        player.goingDown = true;
                        return;
                    }
                    case KeyEvent.VK_RIGHT : {
                        player.goingRight = true;
                        return;
                    }
                    case KeyEvent.VK_LEFT : {
                        player.goingLeft = true;
                        return;
                    }
                }
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        for (GameObject gameObject : handler.gameObjects){
            if (gameObject.id.equals(ID.Player)){
                Player player = (Player) gameObject;
                switch (key){
                    case KeyEvent.VK_UP : {
                        player.goingUp = false;
                        return;
                    }
                    case KeyEvent.VK_DOWN : {
                        player.goingDown = false;
                        return;
                    }
                    case KeyEvent.VK_RIGHT : {
                        player.goingRight = false;
                        return;
                    }
                    case KeyEvent.VK_LEFT : {
                        player.goingLeft = false;
                        return;
                    }
                }
            }
        }

    }
}
