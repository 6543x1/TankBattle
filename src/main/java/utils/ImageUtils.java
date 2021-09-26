package utils;

import panel.GamePanel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageUtils {
    public static BufferedImage p1upImage; //向上移动时的图片
    public static BufferedImage p1downImage;//向下移动时的图片
    public static BufferedImage p1rightImage;//向右移动时的图片
    public static BufferedImage p1leftImage;//向左移动时的图片
    public static BufferedImage brickWall;
    public static BufferedImage base;
    static {
        try {
            p1upImage = ImageIO.read(GamePanel.class.getResource("/img/p1tankU.gif").openStream());
            p1downImage=ImageIO.read((GamePanel.class.getResource("/img/p1tankD.gif").openStream()));
            p1leftImage=ImageIO.read((GamePanel.class.getResource("/img/p1tankL.gif").openStream()));
            p1rightImage=ImageIO.read((GamePanel.class.getResource("/img/p1tankR.gif").openStream()));
            brickWall=ImageIO.read(GamePanel.class.getResource("/img/brickWall.gif").openStream());
            base=ImageIO.read(GamePanel.class.getResource("/img/base.gif").openStream());



        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
