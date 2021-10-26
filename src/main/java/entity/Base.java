package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import utils.ImageUtils;

import java.awt.*;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class Base extends Wall{
    protected int fullHp;
    public Base(int id,int x,int y,int width,int height){
        super(80,id, x, y, width, height);
        image= ImageUtils.base;
        fullHp=80;
    }
    @Override
    public void draw(Graphics2D g2) {
        //血条和蓝条的高度
        //这个paintTank方法也应该写到坦克类中
        int h = 5;
        g2.drawImage(getImage(), getX(), getY(), width, height, null);
        if(hp!=fullHp&&alive){
        g2.setColor(Color.GREEN);
        //System.out.println(hp+","+(double)hp/fullHp);
        if((double)hp/fullHp<0.25){
            g2.setColor(Color.RED);
        }
        else if((double)hp/fullHp<0.5){
            g2.setColor(Color.YELLOW);
        }
            //玩家血条设置为绿色
        //打印基地血条
        g2.draw3DRect(x+(width-fullHp)/2, y-10, fullHp, h, true);
        g2.fill3DRect(x+(width-fullHp)/2, y-10, hp, h, true);
        g2.setColor(Color.WHITE);
        g2.drawString("(" + getX() + "," + getY() + ")", getX(), getY() -20);
        }
        //g2.setColor(Color.BLUE);
        //测试了一下 原来整个panel在第四象限上，y增加往下增长
    }
}
