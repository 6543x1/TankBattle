package entity.Wall;

import utils.ImageUtils;

import java.awt.*;

public class HalfBrickWall extends Wall{
    public HalfBrickWall(int id,int x,int y,int width,int height){
        super(5,id, x, y, width, height);
        image= ImageUtils.getHalfBrickWall();
    }
    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(getImage(), getX(), getY(), width, height, null);
//        g2.setColor(Color.WHITE);
//        g2.drawString("(" + getX() + "," + getY() + ")", getX(), getY() + 1 + 5 + 55);
//        g2.drawString("(" + getX() + "," + getY() + ")", getX(), getY() - 55);
    }
}
