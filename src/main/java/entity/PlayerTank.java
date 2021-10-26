package entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import myEnum.Direction;
import utils.ImageUtils;

import java.awt.*;

import static panel.GamePanel.executorService;

@EqualsAndHashCode(callSuper = true)
@Data
public class PlayerTank extends Tank {
    protected boolean move;
    private int bornCount;

    public PlayerTank(int x, int y, Direction direction, int id) {
        super(x, y, direction, id);
        executorService.submit(new MyTankMove());
        bornCount=300;
    }

    public PlayerTank(int x, int y, int width, int height) {
        super(x, y, width, height);

    }

    public PlayerTank(int x, int y, String url) {
        super(x, y, url);
    }

    class MyTankMove implements Runnable {
        @Override
        public void run() {
            while (alive) {
                while (move) {
                    GetMoveDirection(key);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.out.println(Thread.currentThread().getName() + ":休眠中断");
                    }
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + ":休眠中断");
                }
            }
        }
    }




    @Override
    public void draw(Graphics2D g2) {
        //血条和蓝条的高度
        //这个paintTank方法也应该写到坦克类中
        int h = 5;
        g2.drawImage(getImage(), getX(), getY(), width, height, null);
        if(bornCount>=0){
            int bornCount2=bornCount%4;
            g2.drawImage(ImageUtils.born[bornCount2],getX()-width/2,getY()-height/2,width*2,height*2,null);
            bornCount--;
        }
        if (getId() >= 0) {
            g2.setColor(Color.GREEN);
            //玩家血条设置为绿色
        } else {
            g2.setColor(Color.RED);
        }
        //打印血条
        if(y>=height){
            g2.draw3DRect(getX(), getY() -10, getFullHp(), h, true);
            g2.fill3DRect(getX(), getY() -10, getHP(), h, true);
        }else{
            g2.draw3DRect(getX(), getY() +40, getFullHp(), h, true);
            g2.fill3DRect(getX(), getY() +40, getHP(), h, true);
        }
        g2.setColor(Color.WHITE);
        g2.drawString("(" + getX() + "," + getY() + ")", getX(), getY() + 1 + h + 55);
        g2.drawString("(" + getX() + "," + getY() + ")", getX(), getY() - 55);
        //g2.setColor(Color.BLUE);
        //测试了一下 原来整个panel在第四象限上，y增加往下增长
    }
    @Override
    public void GetMoveDirection(int n){
        int t_x = x/60;
        int t_y = y/60;
        super.GetMoveDirection(n);
//        if (t_y != y/40 || t_x != x/40) {
//                EnemyTank.reCalculatePath=true;
//            }
     }

}
