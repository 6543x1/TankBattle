package entity;

import lombok.Data;
import myEnum.Direction;
import myEnum.Mode;
import myEnum.ObjType;
import panel.GamePanel;
import utils.ImageUtils;

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
                    GameMap.shells.remove(shooter);
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
        for(Wall wall: GameMap.walls.values()){
            if(this.isCollided(wall)){
                alive=false;
                if(wall.getHp()==-1){//-1为不可击毁
                    return true;
                }
                wall.hp-=damage;//因为Shell继承自visualObj Wall也是 而且protected 所以可以直接访问....
                if(wall.getHp()<=0&&!(wall instanceof Base)){
                    GameMap.walls.remove(wall.getId());
                }
                else if(wall.getHp()<=0){
                    //Base
                    wall.setImage(ImageUtils.brokenBase);
                    wall.alive=false;
                    //同时通知Panel GameOver
                }
                GameMap.map[wall.getX()/40][wall.getY()/40]= ObjType.air;
                return true;
            }
        }
        for(Tank tank: GameMap.tanks.values()){
            if(this.isCollided(tank)){
                alive=false;
                if(tank.getHP()==-1){//-1为不可击毁
                    continue;
                }
                tank.HP-=damage;
                if(tank.getHP()<=0){
                    tank.setAlive(false);
                    GameMap.tanks.remove(tank.getId());
                    if(tank.getId()==GamePanel.getP1Tag()||tank.getId()==GamePanel.getP2Tag()){
                        GameMap.reBornPlayer(tank.getId());
                    }
                }
                return true;
            }
        }
        for(Shell shell: GameMap.shells.values()){
            if(this.isCollided(shell)&&!this.equals(shell)){
                alive=false;
                GameMap.walls.remove(shell.getShooter());
                return true;
            }
        }
        return false;
    }
    //备注：枪等小口径武器的叫Bullet（子弹），坦克、战舰等口径大的叫Shell
    //导弹叫Missile
}
