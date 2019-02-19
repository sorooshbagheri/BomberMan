package bomberMan;

import javax.swing.*;
import java.awt.*;


public class Main {

    private static void createAndShowGUI(){
        //Create and set up the window.
        StartFrame startFrame = new StartFrame();
        startFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        startFrame.setTitle("BOMBER MAN");

        //Size and Location customization
        int frameWidth = 550, frameHeight = 400;
        startFrame.setMinimumSize(new Dimension(frameWidth,frameHeight));
//        startFrame.pack();
        startFrame.setResizable(true);
        startFrame.setLocation(new Point((ScreenInfo.getCurrentDisplayMode().getWidth()-frameWidth)/2,(ScreenInfo.getCurrentDisplayMode().getHeight()-frameHeight)/2));


        //Display the window.
        startFrame.setVisible(true);

        //todo remove
//        startFrame.runTheGame();
    }

//    static GraphicsDevice device = GraphicsEnvironment
//            .getLocalGraphicsEnvironment().getScreenDevices()[0];

    public static void main(String[] args) {

        /*
         * display test
         */
//        final JFrame frame = new JFrame("Display Mode");
//        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        frame.setUndecorated(true);
//
//        JButton btn1 = new JButton("Full-Screen");
//        btn1.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                device.setFullScreenWindow(frame);
//            }
//        });
//        JButton btn2 = new JButton("Normal");
//        btn2.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                device.setFullScreenWindow(null);
//            }
//        });
//
//        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
//        panel.add(btn1);
//        panel.add(btn2);
//        frame.add(panel);
//
//        frame.pack();
//        frame.setVisible(true);


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
}