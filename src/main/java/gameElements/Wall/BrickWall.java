package gameElements.Wall;

import lombok.Data;
import lombok.EqualsAndHashCode;
import utils.ImageUtils;

import java.awt.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class BrickWall extends Wall{
    public BrickWall(int id,int x,int y,int width,int height){
        super(10,id, x, y, width, height);
        image= ImageUtils.getBrickWall();
    }
    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(getImage(), getX(), getY(), width, height, null);
        g2.setColor(Color.WHITE);

    }
}
