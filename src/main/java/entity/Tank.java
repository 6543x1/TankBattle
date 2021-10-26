package entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import myEnum.Direction;
import myEnum.ObjType;
import panel.GamePanel;
import utils.ImageUtils;

import java.awt.event.KeyEvent;
import java.util.Stack;
import java.util.concurrent.Future;

import static entity.GameMap.map;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public abstract class Tank extends VisualObj {

    protected int HP;//Hit Points 血量
    protected int fullHp;//总血量
    protected int speed;//移速
    Direction nextDirection;//下一步移动方向（根据接收到的key来判断）
    Direction curDirection;//当前方向
    //下一个位移的坐标
    protected volatile Coordinate next;
    //寻路
    protected Future<Stack<Coordinate>> stackFuture;

    protected int id;
    protected int key;


    public Tank(int x, int y, Direction direction, int id) {
        super(x, y, 40, 40);
        this.fullHp = 40;
        this.HP = 40;
        this.curDirection = direction;
        this.id = id;
//        if (id < Game.PLAY_1) {
//            stackFuture = executorService.submit(new TaskWithPath());
//            executorService.submit(new ETankMove());
//        } else {

//        }
        //executorService.submit(new TankMpRecover());
    }

    public Tank(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public Tank(int x, int y, String url) {
        super(x, y, url);
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
                    setImage(ImageUtils.p1upImage);
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
                    setImage(ImageUtils.p1downImage);
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
                    setImage(ImageUtils.p1leftImage);
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
                    setImage(ImageUtils.p1rightImage);
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
//                if (mp > 0) {
//                    synchronized ("KEY") {
//                        mp -= 10;
//                    }
                //设置子弹的坐标
                //纵横轴坐标需要结合坦克和子弹的图像大小决定
                if(GameMap.shells.get(id)!=null){
                    System.out.println("当前仍有子弹在飞行！射击失败");
                    key=-1;
                    return;
                }
                System.out.println("向"+curDirection+"射击");
                int shellX=x;
                int shellY=y;
                switch (curDirection){
                    case UP:
                        shellX+=(width-Shell.width)/2;
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
                    GameMap.shells.put(getId(),new Shell(shellX,shellY,curDirection,getId(),10));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // break;
            }
        }
        //发生了一整格的变化
        if (t_y != y/height || t_x != x/width) {
            //x、y本来就是*40后放入tank的
            int t_y2 = y / height;
            int t_x2 = x / width;
            //我并没有存储坦克的地图坐标。。。大意了，这样不好寻路？
            //因为移动之后要把原来的点给变成空气
            if (((t_y2 != t_y) || (t_x2 != t_x)) && (x % width == 0 || y % height == 0)) {
                //20%40!=0 !!!!!
//                synchronized (map) {
//                    map[t_y2][t_x2] = map[t_y][t_x];
//                    map[t_y][t_x] = ObjType.air;
//                }//运行到此处出现问题，死锁了？
                map[t_y2][t_x2] = map[t_y][t_x];//此处会导致移动到边界时异常,不是会回滚位置吗？
                map[t_y][t_x] = ObjType.air;
                // coord.x = t_x;
                // coord.y = t_y;
//                if (id == Game.PLAY_1) Game.printMap();

//                if (!executorService.isShutdown()&&this.id!=GamePanel.P1_TAG) {
//                    stackFuture = executorService.submit(new EnemyTank.TaskWithPath());
//                }
                next = null;//很重要！


            }
        }

    }
    //是否可以移动
    protected boolean CheckAndCorrectPosition() {
        //此方法用于纠正位置，比如超出边界、和不可越过物体重叠等问题
        //先根据direction往对应xy加数据 然后判断是否碰撞，最后恢复现场
//        //检测是否碰撞到墙体
        for (Wall wall : GameMap.walls.values()) {
            if (wall.isCollided(this)) {
                return false;
            }
        }
        //检测坦克
        for (Tank tank : GameMap.tanks.values()) {
            if (tank.isCollided(this) && !this.equals(tank)) {
                //如果是玩家就攻击
                return false;
            }
        }
//        Random random=new Random();
//        //电脑随机射击
//        if(id<Game.PLAY_1&&random.nextInt(10)==9){
//            GetKey(16);//随机射击
//        }
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


}
