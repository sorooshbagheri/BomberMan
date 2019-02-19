package bomberMan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Vector;

public class StartFrame extends JFrame {

    private JPanel contentPane;
    private JLabel startLogoLabel;
    private JPanel labelPanel;
    private JPanel rowColPanel;
    private int minRow = 7, maxRow = 31; //must be odd number
    private int minCol = 7, maxCol = 101; // must be odd number
    private JComboBox<Integer> rowComboBox;
    private JComboBox<Integer> colComboBox;

    private JPanel checkBoxPanel;
    private JLabel windowedLabel;
    private JCheckBox windowedCheckBox;
    private JButton startButton;


    public StartFrame() throws HeadlessException {
        init();
    }

    private void init(){

        contentPane = (JPanel) getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane,BoxLayout.PAGE_AXIS));

            //Adding the logo
            ImageIcon startFrameLogo = new ImageIcon(getClass().getResource("Untitled.gif")); //todo manage pics
            startLogoLabel = new JLabel(startFrameLogo);
            startLogoLabel.setBorder(BorderFactory.createEmptyBorder(20,20,10,20));
            startLogoLabel.setAlignmentX(CENTER_ALIGNMENT);
            contentPane.add(startLogoLabel);

            //Adding a JPanel containing the rows and columns label
            labelPanel = new JPanel();
            labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.LINE_AXIS));
            labelPanel.setAlignmentX(CENTER_ALIGNMENT);
            labelPanel.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
                //Adding the labels
                labelPanel.add(new JLabel("---Rows--"));
                labelPanel.add(new JLabel("--Columns---"));
                contentPane.add(labelPanel);

            //Adding a JPanel containing the rows and columns chooser
            rowColPanel = new JPanel();
            rowColPanel.setLayout(new BoxLayout(rowColPanel, BoxLayout.LINE_AXIS));
            rowColPanel.setAlignmentX(CENTER_ALIGNMENT);
            rowColPanel.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
                //Adding the row comboBox
                Vector<Integer> rowOptions = new Vector<>();
                for (int i = minRow; i <= maxRow; i+=2) {
                    rowOptions.add(i);
                }
                rowComboBox = new JComboBox<>(rowOptions);
                rowColPanel.add(rowComboBox);
                //Adding the row comboBox
                Vector<Integer> colOptions = new Vector<>();
                for (int i = minCol; i <= maxCol; i+=2) {
                    colOptions.add(i);
                }
                colComboBox = new JComboBox<>(colOptions);
                rowColPanel.add(colComboBox);
            contentPane.add(rowColPanel);
            rowComboBox.setSelectedIndex(5);
            colComboBox.setSelectedIndex(11);

            //Adding a JPanel containing the checkBox and it's label
            checkBoxPanel = new JPanel();
            checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.LINE_AXIS));
            checkBoxPanel.setAlignmentX(CENTER_ALIGNMENT);
            checkBoxPanel.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
                //Adding the label
                windowedLabel = new JLabel("Windowed");
                checkBoxPanel.add(windowedLabel);
                //Adding the checkbox
                windowedCheckBox = new JCheckBox("",false);
                windowedCheckBox.setMnemonic(KeyEvent.VK_SPACE);
                checkBoxPanel.add(windowedCheckBox);
            contentPane.add(checkBoxPanel);

            //Adding the start button
            startButton = new JButton("START!");
            startButton.setMnemonic(KeyEvent.VK_ENTER);
            startButton.setAlignmentX(CENTER_ALIGNMENT);
//            startButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0,10,10,10),startButton.getBorder()));
            startButton.addActionListener(e -> {
                createAndShowTheGameGUI();
                dispose();

            });
//            startButton.requestFocusInWindow();
            contentPane.add(startButton);

        contentPane.add(Box.createRigidArea(new Dimension(0,20)));



        //preparation and add of display modes to comboBox
//        LinkedHashSet<String> comboBoxItems = new LinkedHashSet<>();
//        for (DisplayMode dm : ScreenInfo.getDisplayModes()){
//            System.out.println(dm.getWidth() + " * " + dm.getHeight());
//            comboBoxItems.add(dm.getWidth() + " * " + dm.getHeight());
//        }
//        JComboBox comboBox = new JComboBox(comboBoxItems.toArray());
//        comboBox.setSize(new Dimension(70,30));
//        contentPane.add(comboBox);



    }

    private void createAndShowTheGameGUI(){
        //TODO Load Images

        //initiation
        windowedCheckBox.setEnabled(false);
        boolean windowed = windowedCheckBox.isSelected();

        //Create and set up the window.
        final double aspectRatio = 4f/3f;
        GameFrame gameFrame = new GameFrame(aspectRatio, windowed,(Integer)rowComboBox.getSelectedItem(),(Integer) colComboBox.getSelectedItem());
        gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("BOMBER MAN");

        //Display the window.
        if (windowed){
            gameFrame.pack();
            gameFrame.setResizable(false);
            gameFrame.setLocation(new Point((ScreenInfo.getCurrentDisplayMode().getWidth()-gameFrame.getWidth())/2,(ScreenInfo.getCurrentDisplayMode().getHeight()-gameFrame.getHeight())/2));
        } else {
            gameFrame.setUndecorated(true);
            ScreenInfo.getDefaultScreenDevice().setFullScreenWindow(gameFrame);
        }
        gameFrame.setVisible(true);
//        gameFrame.requestFocus();
    }

    //todo remove
    void runTheGame(){
//        windowedCheckBox.setSelected(false);
        createAndShowTheGameGUI();
        dispose();
    }

}
