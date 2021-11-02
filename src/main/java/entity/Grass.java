package entity;

import utils.ImageUtils;

import java.awt.*;

public class Grass extends Surface{
    public Grass(int x, int y, int width, int height) {
        super(x, y, width, height);
        image= ImageUtils.grass;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(image,x,y,width,height,null);
    }
}
