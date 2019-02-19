package bomberMan.game;

import bomberMan.GameFrame;
import bomberMan.KeyInput;
import bomberMan.game.gameObjects.GameBoard;
import bomberMan.game.gameObjects.Player;
import bomberMan.game.menu.Menu;
import bomberMan.game.menu.PauseMenu;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.Base64;

//@SuppressWarnings("Duplicates")
public class Game extends JPanel implements Tickable, Runnable {

    public static ImageLoader imageLoader;

    private GameFrame containerFrame;
    private static int gameWidth, gameHeight;
    private Thread gameThread;
    private boolean running = false;
    private int repaints = 0;
    private Menu menu;
    private PauseMenu pauseMenu;
    private GameState gameState;
    private KeyInput keyInput;
    private User user;
    private LevelManager lvlManager;
    private GameBoard currentGameBoard;
    private final String saveFileAddress = "./game.sav";
    public static int rows, columns;

    public void newGame() {
        Player.resetRenderOffset();

        lvlManager = new LevelManager();
        if (user == null) {
            user = new User(lvlManager,rows,columns);
        } else {
            user.setCurrentLvl(1);
        }

        lvlManager.setUser(user);
        currentGameBoard = lvlManager.createLevel(1 );
        keyInput.setPlayer(currentGameBoard.getPlayers().get(0));

        gameState = GameState.inLevel;
    }

    public void load() {
        /*
        try {
//            FileInputStream fin = new FileInputStream(saveFileAddress);

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/BomberMan","root","");
            Statement query = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CLOSE_CURSORS_AT_COMMIT);

            ResultSet result = query.executeQuery("SELECT id,name,gameboard from GameBoards");
            if (result.next()) {
                Blob blob = result.getBlob("GameBoard");

                byte [] data = Base64.getDecoder().decode(blob.getBytes(1, (int) blob.length()));
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                ObjectInputStream ois = new ObjectInputStream(bais);

                pause();

                currentGameBoard = (GameBoard) ois.readObject();
                currentGameBoard.load();

                Player.resetRenderOffset();
                lvlManager = currentGameBoard.getLvlManager();
                lvlManager.createLevel(1);
                lvlManager.setUser(currentGameBoard.getPlayers().get(0).getUser());
                keyInput.setPlayer(currentGameBoard.getPlayers().get(0));

                gameState = GameState.inLevel;
                start();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        */
    }
    public enum GameState{
        inMenu,
        inLevel,
        PauseMenu;
    }

    public Game(Dimension dimension, GameFrame containerFrame, int rows, int columns) {
        Game.imageLoader = new ImageLoader();

        Game.rows = rows;
        Game.columns = columns;

        Game.gameWidth = dimension.width;
        Game.gameHeight = dimension.height;
        this.containerFrame = containerFrame;
        menu = new Menu(this);
        pauseMenu = new PauseMenu(this);
        gameState = GameState.inMenu;

        this.setFocusable(true);
        keyInput = new KeyInput(this,menu, pauseMenu);
        this.addKeyListener(keyInput);
    }

    @Override
    public void run() {
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
//                render();
                repaint();
//                paintImmediately(0,0,Game.getGameWidth(),Game.getGameHeight());
            }
            frames++;

            try {
                Thread.sleep(frames>65 ? 30 : 0);
//                Thread.sleep((long) Math.max(System.nanoTime() - lastTime - ns_per_tick, 1));
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
                repaints = 0;
            }
        }
        stop();
    }

    @Override
    public void tick() {
        switch (gameState){
            case inMenu: menu.tick(); break;
            case inLevel: currentGameBoard.tick(); break;
            case PauseMenu: pauseMenu.tick(); break;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        repaints++;
        g.setColor(Color.BLACK);
        g.fillRect(0,0,gameWidth,gameHeight);
        switch (gameState){
            case inMenu: menu.render(g); break;
            case inLevel: currentGameBoard.render(g); break;
            case PauseMenu: pauseMenu.render(g); break;
        }

//        g.setColor(Color.WHITE);
//        double fontSize= 0.1 * gameHeight * ScreenInfo.getScreenResolution() / 72.0;
//        g.setFont(new Font(null,Font.BOLD|Font.ITALIC, (int) fontSize));
//        g.drawString("Bomber Man!",0,gameHeight/2);


//        g.setColor(Color.BLUE);
//        g.drawLine(0,0,gameWidth,gameHeight);
//        g.drawLine(0,gameHeight,gameWidth,0);

    }

    public void start(){
        //Starting the game loop in a thread else than EDT
        System.out.println("start");
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

    public static int getGameWidth() {
        return gameWidth;
    }

    public static int getGameHeight() {
        return gameHeight;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void exit(){
        running = false;
        containerFrame.exit();
    }

    public boolean isRunning() {
        return running;
    }

    public void pause(){
        running = false;
    }

    public void save(){
        /*
        try {
//            FileWriter fw = new FileWriter(saveFileAddress);
//            FileOutputStream fos = new FileOutputStream(saveFileAddress);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(currentGameBoard);
            oos.close();
//            fos.close();

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/BomberMan","root","");
            Statement query = connection.createStatement();
            query.execute("DELETE FROM GameBoards");

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO GameBoards" + "(ID,NAME,GAMEBOARD) VALUES (?,?,?)");
            preparedStatement.setInt(1,1);
            preparedStatement.setString(2,"Soroosh");
            preparedStatement.setString(3, Base64.getEncoder().encodeToString(baos.toByteArray()));

            preparedStatement.execute();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        */
    }
}
