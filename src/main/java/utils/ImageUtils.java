package utils;

import panel.GamePanel;
import panel.LevelPanel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageUtils {
    private static BufferedImage stage;
    private static BufferedImage p1upImage; //向上移动时的图片
    private static BufferedImage p1downImage;//向下移动时的图片
    private static BufferedImage p1rightImage;//向右移动时的图片
    private static BufferedImage p1leftImage;//向左移动时的图片
    private static BufferedImage brickWall;
    private static BufferedImage base;
    private static BufferedImage brokenBase;
    private static BufferedImage[] born;
    private static BufferedImage steelWall;
    private static BufferedImage halfBrickWall;
    private static BufferedImage enemy1upImage; //向上移动时的图片
    private static BufferedImage enemy1downImage;//向下移动时的图片
    private static BufferedImage enemy1rightImage;//向右移动时的图片
    private static BufferedImage enemy1leftImage;//向左移动时的图片
    private static BufferedImage grass;
    private static BufferedImage heavyUpImage;
    private static BufferedImage heavyDownImage;
    private static BufferedImage heavyLeftImage;
    private static BufferedImage heavyRightImage;
    private static BufferedImage lightUpImage;
    private static BufferedImage lightDownImage;
    private static BufferedImage lightLeftImage;
    private static BufferedImage lightRightImage;
    private static BufferedImage p2upImage;
    private static BufferedImage p2downImage;
    private static BufferedImage p2rightImage;
    private static BufferedImage p2leftImage;
    static {
        try {
            stage=ImageIO.read(LevelPanel.class.getResource("/img/stage.png").openStream());
            p1upImage = ImageIO.read(GamePanel.class.getResource("/img/p1tankU.gif").openStream());
            p1downImage=ImageIO.read((GamePanel.class.getResource("/img/p1tankD.gif").openStream()));
            p1leftImage=ImageIO.read((GamePanel.class.getResource("/img/p1tankL.gif").openStream()));
            p1rightImage=ImageIO.read((GamePanel.class.getResource("/img/p1tankR.gif").openStream()));
            p2upImage = ImageIO.read(GamePanel.class.getResource("/img/p2tankU.gif").openStream());
            p2downImage=ImageIO.read((GamePanel.class.getResource("/img/p2tankD.gif").openStream()));
            p2leftImage=ImageIO.read((GamePanel.class.getResource("/img/p2tankL.gif").openStream()));
            p2rightImage=ImageIO.read((GamePanel.class.getResource("/img/p2tankR.gif").openStream()));
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
            heavyUpImage=ImageIO.read(GamePanel.class.getResource("/img/enemy3U.gif"));
            heavyDownImage=ImageIO.read(GamePanel.class.getResource("/img/enemy3D.gif"));
            heavyLeftImage=ImageIO.read(GamePanel.class.getResource("/img/enemy3L.gif"));
            heavyRightImage=ImageIO.read(GamePanel.class.getResource("/img/enemy3R.gif"));
            lightUpImage=ImageIO.read(GamePanel.class.getResource("/img/enemy2U.gif"));
            lightDownImage=ImageIO.read(GamePanel.class.getResource("/img/enemy2D.gif"));
            lightLeftImage=ImageIO.read(GamePanel.class.getResource("/img/enemy2L.gif"));
            lightRightImage=ImageIO.read(GamePanel.class.getResource("/img/enemy2R.gif"));
        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        }
    }


    public static BufferedImage getStage() {
        return stage;
    }

    public static BufferedImage getP1upImage() {
        return p1upImage;
    }

    public static BufferedImage getP1downImage() {
        return p1downImage;
    }

    public static BufferedImage getP1rightImage() {
        return p1rightImage;
    }

    public static BufferedImage getP1leftImage() {
        return p1leftImage;
    }

    public static BufferedImage getBrickWall() {
        return brickWall;
    }

    public static BufferedImage getBase() {
        return base;
    }

    public static BufferedImage getBrokenBase() {
        return brokenBase;
    }

    public static BufferedImage[] getBorn() {
        return born;
    }

    public static BufferedImage getSteelWall() {
        return steelWall;
    }

    public static BufferedImage getHalfBrickWall() {
        return halfBrickWall;
    }

    public static BufferedImage getEnemy1upImage() {
        return enemy1upImage;
    }

    public static BufferedImage getEnemy1downImage() {
        return enemy1downImage;
    }

    public static BufferedImage getEnemy1rightImage() {
        return enemy1rightImage;
    }

    public static BufferedImage getEnemy1leftImage() {
        return enemy1leftImage;
    }

    public static BufferedImage getGrass() {
        return grass;
    }

    public static BufferedImage getHeavyUpImage() {
        return heavyUpImage;
    }

    public static BufferedImage getHeavyDownImage() {
        return heavyDownImage;
    }

    public static BufferedImage getHeavyLeftImage() {
        return heavyLeftImage;
    }

    public static BufferedImage getHeavyRightImage() {
        return heavyRightImage;
    }

    public static BufferedImage getLightUpImage() {
        return lightUpImage;
    }

    public static BufferedImage getLightDownImage() {
        return lightDownImage;
    }

    public static BufferedImage getLightLeftImage() {
        return lightLeftImage;
    }

    public static BufferedImage getLightRightImage() {
        return lightRightImage;
    }

    public static BufferedImage getP2upImage() {
        return p2upImage;
    }

    public static BufferedImage getP2downImage() {
        return p2downImage;
    }

    public static BufferedImage getP2rightImage() {
        return p2rightImage;
    }

    public static BufferedImage getP2leftImage() {
        return p2leftImage;
    }
}
