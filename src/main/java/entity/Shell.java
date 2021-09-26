package entity;

import lombok.Data;
import panel.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;

@Data
public class Shell extends VisualObj {
    protected Direction direction;
    protected int speed;
    protected int damage;
    protected int shooter;
    public final static int width = 10;
    public final static int height = 10;

    public Shell(int x, int y, Direction direction, int shooter, int damage) throws Exception {
        super(x, y, width, height);
        this.shooter = shooter;
        this.damage = damage;
        this.direction=direction;
        speed = 10;
        setImage(ImageIO.read((GamePanel.class.getResource("/img/defaultShell.gif").openStream())));
        GamePanel.executorService.submit(new ShellMove());
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(image, x, y, width, height, null);

    }

    class ShellMove implements Runnable {
        @Override
        public void run() {
            while (alive) {
                if (isHit()) {
                    alive = false;
                    GamePanel.shells.remove(shooter);
                } else {
                    if (direction == Direction.UP) {
                        y -= speed;
                    } else if (direction == Direction.DOWN) {
                        y += speed;
                    } else if (direction == Direction.LEFT) {
                        x -= speed;
                    } else if (direction == Direction.RIGHT) {
                        x += speed;
                    }
                }
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isHit() {
        if (x < 0 || y < 0 || x > GamePanel.getScreenWidth() || y > GamePanel.getScreenHeight()) {
            return true;
        }
        for(Wall wall:GamePanel.walls.values()){
            if(this.isCollided(wall)){
                alive=false;
                if(wall.getHp()==-1){//-1为不可击毁
                    continue;
                }
                wall.hp-=damage;//因为Shell继承自visualObj Wall也是 而且protected 所以可以直接访问....
                if(wall.getHp()<=0){
                    GamePanel.walls.remove(wall.getId());
                }
                return true;
            }
        }
        for(Tank tank:GamePanel.tanks.values()){
            if(this.isCollided(tank)){
                alive=false;
                if(tank.getHP()==-1){//-1为不可击毁
                    continue;
                }
                tank.HP-=damage;
                if(tank.getHP()<=0){
                    GamePanel.tanks.remove(tank.getId());
                    if(tank.getId()==GamePanel.P1_TAG&&GamePanel.mode==Mode.Single){
                        GamePanel.ShutDown();
                    }
                }
                return true;
            }
        }
        for(Shell shell:GamePanel.shells.values()){
            if(this.isCollided(shell)&&!this.equals(shell)){
                alive=false;
                GamePanel.walls.remove(shell.getShooter());
                return true;
            }
        }
        return false;
    }
    //备注：枪等小口径武器的叫Bullet（子弹），坦克、战舰等口径大的叫Shell
    //导弹叫Missile
}
