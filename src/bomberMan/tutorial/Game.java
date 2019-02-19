package bomberMan.tutorial;

import bomberMan.ScreenInfo;
import sun.awt.image.BufferedImageGraphicsConfig;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Game extends Canvas implements Runnable {

    public static final int WIDTH = 1080, HEIGHT = WIDTH/4*3;

    private Thread gameThread;
    private boolean running = false;

    private Handler handler;

    BufferedImage bufferedImage;
    BufferedImage scaled;
    public Game() {
        setBackground(Color.black);
        setPreferredSize(new Dimension(10000,10000));

        handler = new Handler();
        this.addKeyListener(new KeyInput(handler));

        new Window(WIDTH,HEIGHT,"tutorial.Game",this);

        handler.addObject(new Player(50,50,ID.Player));
        handler.addObject(new Enemy(100,100,ID.Enenmy));
        handler.addObject(new Enemy(200,100,ID.Enenmy));
        handler.addObject(new Enemy(200,200,ID.Enenmy));
        handler.addObject(new Enemy(100,200,ID.Enenmy));



        try {
            bufferedImage = ImageIO.read(getClass().getResource("Screenshot1.png"));
//            scaled = new BufferedImage(160,90,bufferedImage.getType());
            scaled = ScreenInfo.getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(160,90);
            scaled.getGraphics().drawImage(bufferedImage,0,0,160,90,null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void start(){
        gameThread = new Thread(this);
        gameThread.start();
        running = true;
    }

    public synchronized void stop(){
        try {
            gameThread.join();
            running = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    int repaints = 0;

    @Override
    public void run() {
        requestFocus();

        //The Game Loop
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0; //per second
        double ns_per_tick = 1000000000 / amountOfTicks; //results in almost: 16 millions
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (running){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns_per_tick;
            lastTime = now;
            while (delta >= 1){
                tick(); //must take less than 16 million nano seconds
                delta --;
            }
            if (running) {
                render();
//                repaint();
            }
            frames++;

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            if (frames>60){
//                try {
//                    Thread.sleep(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }

            if (System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                System.out.format("FPS: %d repaints: %d%n", frames, repaints);
                frames = 0;
                repaints =0 ;
            }
        }
        stop();
    }

    private void tick(){
        handler.tick();
    }

    private void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null){
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.black);
        g.fillRect(0,0,WIDTH,HEIGHT);
        handler.render(g);

//        g.drawRect(100,100,50,50);
//        g.drawRect(200,200,50,50);
//        g.drawRect(200,100,50,50);
//        g.drawRect(100,200,50,50);


        g.drawImage(scaled,0,0,WIDTH,HEIGHT,null);


        repaints++;

        g.dispose();
        bs.show();

    }

//    Random random = new Random();
//    @Override
//    public void paintComponent(Graphics g) {
//        synchronized (this) {
//            super.paintComponent(g);
//
//            //TODO DO THE RENDERING
//            g.setColor(Color.black);
//            g.fillRect(100, random.nextInt(1000), 100, 100);
//            repaints++;
//        }
//    }


    public static void main(String[] args) {
        new Game();
    }
}
