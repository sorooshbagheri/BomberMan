package bomberMan.tutorial;

import java.awt.*;

public class Enemy extends GameObject {

    public Enemy(int x, int y, ID id) {
        super(x, y, id);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        g.drawRect(x,y,50,50);
    }
}
