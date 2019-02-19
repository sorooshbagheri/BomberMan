package bomberMan.tutorial;

import java.awt.*;

public class Player extends GameObject {
    boolean goingUp, goingDown, goingRight, goingLeft;

    public Player(int x, int y, ID id) {
        super(x, y, id);
        velX = 0;
        velY = 0;

    }

    @Override
    public void tick() {
        velX = 0;
        velY = 0;
        if (goingUp) velY = -1;
        if (goingDown) velY = 1;
        if (goingRight) velX = 1;
        if (goingLeft) velX = -1;
        x += velX;
        y += velY;

        int[] xs = {x,x+20,x+40,x+20};
        int[] ys = {y+20,y,y+20,y+40};
        Polygon polygon = new Polygon(xs,ys,4);
        if (polygon.intersects(new Rectangle(100,100,50,50))) System.out.println("intersects 1");
        if (polygon.intersects(new Rectangle(200,100,50,50))) System.out.println("intersects 2");
        if (polygon.intersects(new Rectangle(100,200,50,50))) System.out.println("intersects 3");
        if (polygon.intersects(new Rectangle(200,200,50,50))) System.out.println("intersects 4");

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.white);
        g.drawRect(x,y,40,40);
        g.drawLine(x,y,x+40,y+40);
        g.drawLine(x,y+40,x+40,y);
        int[] xs = {x,x+20,x+40,x+20};
        int[] ys = {y+20,y,y+20,y+40};
        g.drawPolygon(new Polygon(xs,ys,4));
    }


}
