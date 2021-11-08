package utils;

import panel.GamePanel;
import panel.LevelPanel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageUtils {
    public static BufferedImage stage;
    public static BufferedImage p1upImage; //向上移动时的图片
    public static BufferedImage p1downImage;//向下移动时的图片
    public static BufferedImage p1rightImage;//向右移动时的图片
    public static BufferedImage p1leftImage;//向左移动时的图片
    public static BufferedImage brickWall;
    public static BufferedImage base;
    public static BufferedImage brokenBase;
    public static BufferedImage[] born;
    public static BufferedImage steelWall;
    public static BufferedImage halfBrickWall;
    public static BufferedImage enemy1upImage; //向上移动时的图片
    public static BufferedImage enemy1downImage;//向下移动时的图片
    public static BufferedImage enemy1rightImage;//向右移动时的图片
    public static BufferedImage enemy1leftImage;//向左移动时的图片
    public static BufferedImage grass;
    static {
        try {
            stage=ImageIO.read(LevelPanel.class.getResource("/img/stage.png").openStream());
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
            steelWall=ImageIO.read(GamePanel.class.getResource("/img/steels.gif").openStream());
            halfBrickWall=ImageIO.read(GamePanel.class.getResource("/img/halfBrickWall.gif").openStream());
            enemy1upImage = ImageIO.read(GamePanel.class.getResource("/img/enemy1U.gif").openStream());
            enemy1downImage=ImageIO.read((GamePanel.class.getResource("/img/enemy1D.gif").openStream()));
            enemy1leftImage=ImageIO.read((GamePanel.class.getResource("/img/enemy1L.gif").openStream()));
            enemy1rightImage=ImageIO.read((GamePanel.class.getResource("/img/enemy1R.gif").openStream()));
            grass=ImageIO.read(GamePanel.class.getResource("/img/grass.png").openStream());

        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        }
    }


}
