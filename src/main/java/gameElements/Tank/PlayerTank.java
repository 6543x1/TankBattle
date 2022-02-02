package gameElements.Tank;

import myEnum.Direction;
import utils.ImageUtils;
import utils.SettingsUtils;

import java.awt.*;

import static panel.GamePanel.getExecutorService;


public class PlayerTank extends Tank {
    public void setMove(boolean move) {
        this.move = move;
    }

    protected boolean move;
    private int bornCount;

    public PlayerTank(int x, int y, Direction direction, int id) {
        super(x, y, direction, id);
        if(id==1){
        getExecutorService().execute(new MyTankMove());}
        bornCount=300;
        super.HP=100;
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
    protected String getName() {
        return SettingsUtils.readGameSettings().getPlayerName();
    }

    @Override
    public void draw(Graphics2D g2) {

        //这个paintTank方法也应该写到坦克类中
        int h = 5;
        g2.drawImage(getImage(), getX(), getY(), width, height, null);
        if(bornCount>=0){
            int bornCount2=bornCount%4;
            g2.drawImage(ImageUtils.getBorn()[bornCount2],getX()-width/2,getY()-height/2,width*2,height*2,null);
            bornCount--;
        }
        if (getId() >= 0) {
            g2.setColor(Color.GREEN);
            //玩家血条设置为绿色
        } else {
            g2.setColor(Color.RED);
        }
        g2.setColor(Color.GREEN);
        g2.drawString(name,getX()-5,getY()-10);
    }
    @Override
    public void GetMoveDirection(int n){
        super.GetMoveDirection(n);
     }
     @Override
    protected void changeDirectionImage(){
        switch (curDirection){
            case UP:
                setImage(ImageUtils.getP1upImage());
                break;
            case DOWN:
                setImage(ImageUtils.getP1downImage());
                break;
            case LEFT:
                setImage(ImageUtils.getP1leftImage());
                break;
            case RIGHT:
                setImage(ImageUtils.getP1rightImage());
                break;
        }
    }

}
