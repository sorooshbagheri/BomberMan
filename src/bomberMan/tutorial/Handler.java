package bomberMan.tutorial;

import java.awt.*;
import java.util.LinkedList;

public class Handler implements tickable, renderable {

    LinkedList<GameObject> gameObjects = new LinkedList<>();
    
    @Override
    public void render(Graphics g) {
        for (GameObject gameObject : gameObjects) {
            gameObject.render(g);
        }
    }

    @Override
    public void tick() {
        for (GameObject gameObject : gameObjects) {
            gameObject.tick();
        }
    }

    public void addObject(GameObject gameObject){
        gameObjects.add(gameObject);
    }

    public void removeObject(GameObject gameObject){
        gameObjects.remove(gameObject);
    }

    public LinkedList<GameObject> getGameObjects() {
        return gameObjects;
    }
}
