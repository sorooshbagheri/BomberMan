package bomberMan.game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageLoader {
    public BufferedImage player;
    public BufferedImage enemyLvl1;
    public BufferedImage enemyLvl2;
    public BufferedImage enemyLvl3;
    public BufferedImage enemyLvl4;


    public ImageLoader() {
        try {
            this.player = ImageIO.read(getClass().getResource("../res/player.png"));
            this.enemyLvl1 = ImageIO.read(getClass().getResource("../res/enemyLvl1.png"));
            this.enemyLvl2 = ImageIO.read(getClass().getResource("../res/enemyLvl2.png"));
            this.enemyLvl3 = ImageIO.read(getClass().getResource("../res/enemyLvl3_no_cloth.png"));
            this.enemyLvl4 = ImageIO.read(getClass().getResource("../res/enemyLvl4.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getSheet(String name) throws NoSuchFieldException, IllegalAccessException {
        return (BufferedImage) this.getClass().getField(name).get(this);
    }
}
