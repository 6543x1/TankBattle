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
    public static BufferedImage brokenBase;
    public static BufferedImage[] born;
    static {
        try {
            p1upImage = ImageIO.read(GamePanel.class.getResource("/img/p1tankU.gif").openStream());
            p1downImage=ImageIO.read((GamePanel.class.getResource("/img/p1tankD.gif").openStream()));
            p1leftImage=ImageIO.read((GamePanel.class.getResource("/img/p1tankL.gif").openStream()));
            p1rightImage=ImageIO.read((GamePanel.class.getResource("/img/p1tankR.gif").openStream()));
            brickWall=ImageIO.read(GamePanel.class.getResource("/img/brickWall.gif").openStream());
            base=ImageIO.read(GamePanel.class.getResource("/img/base.gif").openStream());
            brokenBase=ImageIO.read(GamePanel.class.getResource("/img/brokenBase.gif").openStream());
            born=new BufferedImage[4];
            for(int i=0;i<4;i++){
                born[i]=ImageIO.read(GamePanel.class.getResource("/img/born"+(i+1)+".gif").openStream());
            }


        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        }
    }


}
