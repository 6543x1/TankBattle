package gameElements.Tank;

import gameElements.*;
import gameElements.Map.GameMap;
import gameElements.Shell.Shell;
import gameElements.Wall.Wall;
import myEnum.Direction;
import panel.GamePanel;

import java.awt.event.KeyEvent;
import java.util.Stack;
import java.util.concurrent.Future;


public abstract class Tank extends VisualObj {

    protected int HP;//Hit Points 血量
    protected int speed;//移速
    Direction curDirection;//当前方向
    //下一个位移的坐标
    protected volatile Coordinate next;
    //寻路
    protected Future<Stack<Coordinate>> stackFuture;

    protected int id;
    protected int key;
    protected String name;

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Direction getCurDirection() {
        return curDirection;
    }

    public void setCurDirection(Direction curDirection) {
        this.curDirection = curDirection;
    }

    public Coordinate getNext() {
        return next;
    }

    public void setNext(Coordinate next) {
        this.next = next;
    }

    public Future<Stack<Coordinate>> getStackFuture() {
        return stackFuture;
    }

    public void setStackFuture(Future<Stack<Coordinate>> stackFuture) {
        this.stackFuture = stackFuture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Tank(int x, int y, Direction direction, int id) {
        super(x, y, 40, 40);
        this.HP = 20;
        this.curDirection = direction;
        this.id = id;
        this.name=getName();
    }


    public void GetMoveDirection(int n) {
        int t_x = x/width;
        int t_y = y/height;
        //判断按键
        switch (n) {
            //判断移动基本上都是先假设已经移动然后判断移动之后是否会发生重叠
            //如果坐标发生了一整格的改变，那就更新map
            case KeyEvent.VK_UP: {

                //先判断坦克的方向，方向相同则判断是否可以移动

                if (!(curDirection == Direction.UP)) {
                    curDirection = Direction.UP;
//                    setImage(ImageUtils.p1upImage);
                    //nextDirection = Direction.UP;
                } else {
                    y -= speed;
                    //已经转向当前方向，向上移动（注意是第四象限）
                    //然后再检查一下当前位置是否合理，如果不合理直接纠正到合理位置
                    //为什么不先检查呢？因为这样当只差一点点到边缘的时候，可能因为速度过快，无法到达边缘位置，这和到达边缘后再越界不同
                    //这样得分情况返回结果，太麻烦了。。。
                    if(!CheckAndCorrectPosition()){
                        y+=speed;//难以纠正方位，回滚位置
                    }
                }
                break;
            }
            case KeyEvent.VK_DOWN: {
                //先判断坦克的方向，方向相同则判断是否可以移动

                if (!(curDirection == Direction.DOWN)) {
                    curDirection = Direction.DOWN;
//                    setImage(ImageUtils.p1downImage);
                } else {
                    y += speed;//已经转向当前方向，向上移动（注意是第四象限）
                    if(!CheckAndCorrectPosition()){
                        y-=speed;//难以纠正方位，回滚位置
                    }
                }
                break;
            }
            case KeyEvent.VK_LEFT: {
                //先判断坦克的方向，方向相同则判断是否可以移动
                if (!(curDirection == Direction.LEFT)) {
                    curDirection = Direction.LEFT;
//                    setImage(ImageUtils.p1leftImage);
                } else {
                    x -= speed;//已经转向当前方向，向上移动（注意是第四象限）
                    if(!CheckAndCorrectPosition()){
                        x+=speed;//难以纠正方位，回滚位置
                    }
                }
                break;
            }
            case KeyEvent.VK_RIGHT: {
                //先判断坦克的方向，方向相同则判断是否可以移动

                if (!(curDirection == Direction.RIGHT)) {
                    curDirection = Direction.RIGHT;
//                    setImage(ImageUtils.p1rightImage);
                } else {
                    x += speed;//已经转向当前方向，向上移动（注意是第四象限）
                    if(!CheckAndCorrectPosition()){
                        x-=speed;//难以纠正方位，回滚位置
                    }
                }
                break;
            }
            case KeyEvent.VK_NUMPAD0: {
                //子弹射出
                //先判断是否有子弹正在途中
                //设置子弹的坐标
                //纵横轴坐标需要结合坦克和子弹的图像大小决定
                if(GameMap.getShells().get(id)!=null){
//                    System.out.println("当前仍有子弹在飞行！射击失败");
                    key=-1;
                    return;
                }
//                System.out.println("向"+curDirection+"射击");
                int shellX=x;
                int shellY=y;
                switch (curDirection){
                    case UP:
                        shellX+=(width- Shell.width)/2;
                        shellY-=Shell.height;
                        break;
                    case DOWN:
                        shellX+=(width-Shell.width)/2;
                        shellY+=height+Shell.height;
                        break;
                    case LEFT:
                        shellX-=Shell.width;
                        shellY+=(height-Shell.height)/2;
                        break;
                    case RIGHT:
                        shellX+=width+Shell.width;
                        shellY+=(height-Shell.height)/2;
                        break;
                }
                try {
                    GameMap.getShells().put(getId(),new Shell(shellX,shellY,curDirection,getId(),10));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // break;
            }
        }
        changeDirectionImage();
        //发生了一整格的变化
        if (t_y != y/height || t_x != x/width) {
            //x、y本来就是*40后放入tank的
            int t_y2 = y / height;
            int t_x2 = x / width;
            //我并没有存储坦克的地图坐标。。。大意了，这样不好寻路？
            //因为移动之后要把原来的点给变成空气
            if (((t_y2 != t_y) || (t_x2 != t_x)) && (x % width == 0 || y % height == 0)) {

                next = null;//很重要！


            }
        }

    }
    //是否可以移动
    protected boolean CheckAndCorrectPosition() {
        //此方法用于纠正位置，比如超出边界、和不可越过物体重叠等问题
        //先根据direction往对应xy加数据 然后判断是否碰撞，最后恢复现场
//        //检测是否碰撞到墙体
        for (Wall wall : GameMap.getWalls().values()) {
            if (wall.isCollided(this)) {
                return false;
            }
        }
        //检测坦克
        for (Tank tank : GameMap.getTanks().values()) {
            if (tank.isCollided(this) && !this.equals(tank)) {
                //如果是玩家就攻击
                return false;
            }
        }
        if (x < 0) {
            x = 0;
        } else if (x > GamePanel.getScreenWidth() - width) {
            x = GamePanel.getScreenWidth() - width;
        }
        if (y < 0) {
            y = 0;
        } else if (y > GamePanel.getScreenHeight() - height) {
            y = GamePanel.getScreenHeight() - height;
        }
        return true;
    }
    protected  abstract void changeDirectionImage();
    protected abstract String getName();


}
