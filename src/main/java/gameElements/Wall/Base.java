package gameElements.Wall;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import utils.ImageUtils;

import java.awt.*;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class Base extends Wall {
    protected int fullHp;
    public Base(int id,int x,int y,int width,int height){
        super(10,id, x, y, width, height);
        image= ImageUtils.getBase();
        fullHp=10;
    }
    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(getImage(), getX(), getY(), width, height, null);

    }
}
