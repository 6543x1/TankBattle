package gameElements.Shell;

import gameElements.Tank.*;
import gameElements.Wall.Base;
import gameElements.Map.GameMap;
import gameElements.VisualObj;
import gameElements.Wall.Wall;
import myEnum.Direction;
import myEnum.ObjType;
import panel.GamePanel;
import utils.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;


public class Shell extends VisualObj {
    private final Direction direction;
    private final int speed;
    private int damage;
    private int shooter;
    public final static int width = 10;
    public final static int height = 10;

    public Shell(int x, int y, Direction direction, int shooter, int damage) throws Exception {
        super(x, y, width, height);
        this.shooter = shooter;
        this.damage = damage;
        this.direction=direction;
        speed = 10;
        setImage(ImageIO.read((GamePanel.class.getResource("/img/defaultShell.gif").openStream())));
        GamePanel.getExecutorService().submit(new ShellMove());
    }
    public void setDamage(int damage) {
        this.damage = damage;
    }
    public int getShooter() {
        return shooter;
    }

    public void setShooter(int shooter) {
        this.shooter = shooter;
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
                    GameMap.getShells().remove(shooter);
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
        for(Wall wall: GameMap.getWalls().values()){
            if(this.isCollided(wall)){
                alive=false;
                if(wall.getHp()==-1){//-1为不可击毁
                    return true;
                }
                wall.setHp(wall.getHp()-damage);//因为Shell继承自visualObj Wall也是 而且protected 所以可以直接访问....
                if(wall.getHp()<=0&&!(wall instanceof Base)){
                    GameMap.getWalls().remove(wall.getId());
                }
                else if(wall.getHp()<=0){
                    //Base
                    wall.setImage(ImageUtils.getBrokenBase());
                    wall.setAlive(false);
                    GamePanel.reStart();
                    //同时通知Panel GameOver
                }
                GameMap.getMap()[wall.getX()/40][wall.getY()/40]= ObjType.air;
                return true;
            }
        }
        for(Tank tank: GameMap.getTanks().values()){
            if(this.isCollided(tank)){
                alive=false;
                if(tank.getHP()==-1){//-1为不可击毁
                    continue;
                }
                tank.setHP(tank.getHP()-damage);
                if(tank.getHP()<=0){
                    tank.setAlive(false);
                    GameMap.getTanks().remove(tank.getId());
                    if(tank instanceof PlayerTank){
                        GameMap.reBornPlayer(tank.getId());
                    }
                    else if(tank instanceof EnemyTank){
                        if(tank instanceof EnemyLightTank){
                            GameMap.reBornEnemy();
                        }
                        else if(tank instanceof EnemyHeavyTank){
                            GameMap.reBornEnemy();
                        }
                        else{
                        GameMap.reBornEnemy();
                        }
                    }
                }
                return true;
            }
        }
        for(Shell shell: GameMap.getShells().values()){
            if(this.isCollided(shell)&&!this.equals(shell)){
                alive=false;
                GameMap.getWalls().remove(shell.getShooter());
                return true;
            }
        }
        return false;
    }
    //备注：枪等小口径武器的叫Bullet（子弹），坦克、战舰等口径大的叫Shell
    //导弹叫Missile
}
