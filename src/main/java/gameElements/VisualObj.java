package gameElements;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

@Data
@NoArgsConstructor
public abstract class VisualObj implements Serializable {
    protected int x;
    protected int y;
    protected BufferedImage image;
    protected boolean alive;//是否存在
    protected int height;
    protected int width;
    public VisualObj(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.alive=true;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);//实例化图片
    }

    /**
     * 构造方法
     * @param x -横坐标
     * @param y -纵坐标
     * @param url -图片路径
     */
    public VisualObj(int x, int y, String url) {
        this.x = x;
        this.y = y;
        try {
            image = ImageIO.read(new File(url));//获取此路径的图片对象
            this.width = image.getWidth();
            this.height = image.getHeight();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public abstract void draw(Graphics2D g2);

    private Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isCollided(VisualObj visualObj) {
        return this.getRect().intersects(visualObj.getRect());//判断两者是否碰撞
    }
}
