package bomberMan;

import bomberMan.game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class GameFrame extends JFrame {

    final private float heightPercentageOfScreen = 0.8f;    //while windowed
    final private double aspectRatio;
    final private boolean windowed;
    private JPanel contentPanePanel;
    private int rows, columns;

    public GameFrame(double aspectRatio, boolean windowed, Integer rows, Integer columns) throws HeadlessException {
        this.aspectRatio = aspectRatio;
        this.windowed = windowed;
        this.rows = rows;
        this.columns = columns;
        init();
    }

    private void init() {

        //Setting up the ContentPanePanel
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        contentPanePanel = new JPanel();
        //Size customization
        int cpPanelHeight;
        int cpPanelWidth;
        if (windowed) {
            cpPanelHeight = (int) (heightPercentageOfScreen * ScreenInfo.getCurrentDisplayMode().getHeight());
        } else {
            cpPanelHeight = ScreenInfo.getCurrentDisplayMode().getHeight();
        }
        cpPanelWidth = (int) (cpPanelHeight * aspectRatio);
        Dimension cpPanelDimension = new Dimension(cpPanelWidth, cpPanelHeight);

        contentPanePanel.setMinimumSize(cpPanelDimension);
        contentPanePanel.setPreferredSize(cpPanelDimension);
        contentPanePanel.setMaximumSize(cpPanelDimension);
        contentPanePanel.setAlignmentX(CENTER_ALIGNMENT);
        getContentPane().setPreferredSize(cpPanelDimension);

        contentPanePanel.setLayout(new BoxLayout(contentPanePanel, BoxLayout.PAGE_AXIS));
        contentPanePanel.setBackground(Color.RED);
        getContentPane().setBackground(Color.BLACK);
        getContentPane().add(contentPanePanel);


//        Canvas canvas = new Canvas();
//        contentPanePanel.add(canvas);
        Game game = new Game(cpPanelDimension, this, rows, columns);
        contentPanePanel.add(game); //since Game extends JPanel
        game.setPreferredSize(cpPanelDimension);
        game.start();


//        System.out.format("xLocation: %d%nyLocation: %d%nwidth %d%nheight: %d%n", getLocation().x,getLocation().y,getWidth(), getHeight());

    }

    public void exit() {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }


}