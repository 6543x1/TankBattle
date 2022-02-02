package gameElements.Tank;

import gameElements.Coordinate;
import gameElements.Map.GameMap;
import myEnum.Difficulty;
import myEnum.Direction;
import myEnum.ObjType;
import panel.GamePanel;
import utils.ImageUtils;
import utils.SettingsUtils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;

import static panel.GamePanel.*;

public class EnemyTank extends Tank {
    private int count;
    private final Coordinate target;
    private final int shotCD;
    public EnemyTank(int x, int y, Direction direction, int id) {
        super(x, y, direction, id);

        count=0;
        if(SettingsUtils.readGameSettings().getDifficulty()== Difficulty.easy){
            shotCD=1000;
             target= new Coordinate(GameMap.getTanks().get(GamePanel.getP1Tag()).getX()/width, GameMap.getTanks().get(GamePanel.getP1Tag()).getY()/height);
        }
        else if(SettingsUtils.readGameSettings().getDifficulty()==Difficulty.normal){
            shotCD=800;
            target = new Coordinate(GameMap.getBaseX()/width, GameMap.getBaseY()/height);
        }
        else{
            shotCD=400;
            target = new Coordinate(GameMap.getBaseX()/width, GameMap.getBaseY()/height);
        }
        stackFuture = getExecutorService().submit(new TaskWithPath());//这一步很重要！！！
        getExecutorService().submit(new TraceMove());
        getExecutorService().submit(new RandomShoot());
    }
    class RandomShoot implements Runnable{
        @Override
        public void run(){
            Random random=new Random(System.currentTimeMillis());
            while(alive){
                if(random.nextInt(100)<20){
                    GetMoveDirection(KeyEvent.VK_NUMPAD0);
                }
                try {
                    Thread.sleep(shotCD);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    class TraceMove implements Runnable{
        @Override
        public void run(){
            //还有一种解决办法：如果移动的距离没有超过一个身位，就保持当前方向
            while (alive) {
                Coordinate coordinate=new Coordinate(getX(),getY());
                Direction direction=curDirection;
                if (stackFuture.isDone()) {
                    try {
                        //使用栈存放广度遍历算法得到的移动的路径
                        Stack<Coordinate> result = stackFuture.get();
                        if (null != result && result.size() != 0 && null == next) {
                            next = result.pop();
                        }
                        if(next!=null&& GameMap.getMap()[next.getX()][next.getY()]!=ObjType.air&& GameMap.getMap()[next.getX()][next.getY()]!=ObjType.surface){
                            System.out.println("Not able to Move to"+next.getX()+","+next.getY());
                            Thread.sleep(1000);
                            stackFuture = getExecutorService().submit(new TaskWithPath());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Coordinate coord=new Coordinate(x/40,y/40);
                if (null != next && !next.equals(coord)) {
                    if (Math.abs(coord.getX() - next.getX()) > 1 || Math.abs(coord.getY() - next.getY()) > 1) {
                        System.out.println(Thread.currentThread().getName() + ":" + coord + "->" + next.toString());
                    }
                    if(x%40!=0||y%40!=0){
                        int n;
                        switch (direction) {
                            case UP:
                                n = KeyEvent.VK_UP;
                                break;
                            case DOWN:
                                n = KeyEvent.VK_DOWN;
                                break;
                            case LEFT:
                                n = KeyEvent.VK_LEFT;
                                break;
                            case RIGHT:
                                n = KeyEvent.VK_RIGHT;
                                break;
                            default:
                                n = KeyEvent.VK_NUMPAD0;
                        }
                        GetMoveDirection(n);
                    }
                    else {
                        GetMoveDirection(GetDirection(coord, next));
                    }
                }
                if(next==null||next.equals(coord)){
                    GetMoveDirection(RandomMove());
                }
                if(new Coordinate(getX(),getY()).equals(coordinate)){
                    GetMoveDirection(RandomMove());
                }
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + ":休眠中断");
                }
            }
        }
    }
    private int GetDirection(Coordinate coord, Coordinate next) {
        int n;
        if (next == null) {
            return RandomMove();
        }
        if (coord.getX() - next.getX() <= -1) {
            n = KeyEvent.VK_RIGHT;
        } else if (coord.getX() - next.getX() >= 1) {
            n = KeyEvent.VK_LEFT;
        } else {
            if (coord.getY() - next.getY() <= -1) {
                n = KeyEvent.VK_DOWN;
            } else {
                n = KeyEvent.VK_UP;
            }
        }
        return n;
    }

    private int RandomMove() {
        if(++count<20){
            int n;
            switch (curDirection) {
                case DOWN:
                    n = KeyEvent.VK_DOWN;
                    break;
                case UP:
                    n = KeyEvent.VK_UP;
                    break;
                case RIGHT:
                    n = KeyEvent.VK_RIGHT;
                    break;
                case LEFT:
                    n = KeyEvent.VK_LEFT;
                    break;
                default:
                    n = KeyEvent.VK_NUMPAD0;
            }
            return n;
        }
        int[] ops = {KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_NUMPAD0};
        Random random = new Random(System.currentTimeMillis());
        return ops[random.nextInt(4)];
    }


    @Override
    public void draw(Graphics2D g2) {
        int h = 5;
        g2.drawImage(getImage(), getX(), getY(), width, height, null);

        g2.setColor(Color.RED);

        g2.setColor(Color.WHITE);
        g2.drawString(name,getX(),getY()-10);

    }

    @Override
    protected String getName() {
            StringBuilder val = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < 6; i++) {
                // 输出字母还是数字
                String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
                // 字符串
                if ("char".equalsIgnoreCase(charOrNum)) {
                    int choice = 65;//默认用大写了，六位重复率应该比较低吧
                    val.append((char) (choice + random.nextInt(26)));
                } else { // 数字
                    val.append(random.nextInt(10));
                }
            }
            return val.toString();
    }

    @Override
    public void GetMoveDirection(int n) {
        int t_x = x/width;
        int t_y = y/height;
        Random random=new Random();
        //电脑随机射击
        if(random.nextInt(10)<=2){
            super.GetMoveDirection(KeyEvent.VK_NUMPAD0);
            try {
                Thread.sleep(50);//避免自己射击自己
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        super.GetMoveDirection(n);
        // 如果坐标发生了一整格的变化，就更新二维数组->方便寻路系统
        if (t_y != y/height || t_x != x/width) {
            //x、y本来就是*40后放入tank的
            int t_y2 = y / height;
            int t_x2 = x / width;
            //我并没有存储坦克的地图坐标。。。大意了，这样不好寻路？
            //因为移动之后要把原来的点给变成空气
            if (((t_y2 != t_y) || (t_x2 != t_x)) && (x % height == 0 || y % width == 0)) {
                if (!getExecutorService().isShutdown()) {
                    stackFuture = getExecutorService().submit(new TaskWithPath());
                }
                next = null;//很重要！


            }
        }
       changeDirectionImage();

    }
    @Override
    protected void changeDirectionImage(){
        switch (curDirection){
            case UP:
                setImage(ImageUtils.getEnemy1upImage());
                break;
            case DOWN:
                setImage(ImageUtils.getEnemy1downImage());
                break;
            case LEFT:
                setImage(ImageUtils.getEnemy1leftImage());
                break;
            case RIGHT:
                setImage(ImageUtils.getEnemy1rightImage());
                break;
        }
    }


    //路径获得的线程
    class TaskWithPath implements Callable<Stack<Coordinate>> {

        /**
         * 任务的具体过程，一旦任务传给ExecutorService的submit方法，
         * 则该方法自动在一个线程上执行
         */
        @Override
        public Stack<Coordinate> call() {
            //该返回结果将被Future的get方法得到
//            Random random = new Random();
//            Thread.sleep(random.nextInt(50));
            return GetPath();
        }
    }

    /**
     * 使用广度遍历算法，使用队列存储遍历的节点
     *
     * @return 移动的路径
     */
    private Stack<Coordinate> GetPath() {
        Stack<Coordinate> Coordinates = new Stack<>();
        Coordinate target = new Coordinate(GameMap.getTanks().get(GamePanel.getP1Tag()).getX()/width, GameMap.getTanks().get(GamePanel.getP1Tag()).getY()/height);
        if(GameMap.getMap()[target.getX()][target.getY()]==ObjType.surface){
            return Coordinates;
        }
        Queue<Coordinate> queue = new LinkedBlockingQueue<>();
        HashSet<Coordinate> checkSet=new HashSet<>();
        Coordinate coordinate = new Coordinate(getX() /width, y/ getHeight());
//        int[] a= {2,3,4,5,6};
//        int b=Arrays.stream(a).max().getAsInt();
        checkSet.add(coordinate);
        queue.offer(coordinate);//当前坐标入队，若入队失败抛出异常
        Coordinate last = null;
        boolean flag=false;
        while (!queue.isEmpty()) {
            Coordinate point = queue.poll();//队头出列
           // System.out.println("t:"+point);
//            　　抛出异常 　　返回特殊值
//            插入：add(e) 　　  offer(e)  插入一个元素
//            移除：remove()      poll()      移除和返回队列的头
//            检查：element()     peek()    返回但不移除队列的头。
            int tx = point.getX();
            int ty = point.getY();
            int i;
            //遍历所有的方向
//            Random r = new Random(System.currentTimeMillis());
            for (i = 0; i < 4; ++i) {
                Direction direction = Direction.values()[i];
                switch (direction) {
                    case UP:
                        ty -= 1;
                        break;
                    case LEFT:
                        tx -= 1;
                        break;
                    case RIGHT:
                        tx += 1;
                        break;
                    case DOWN:
                        ty += 1;
                        break;
                }
                //判断该点是否可行
                flag = true;
                Coordinate nextStep = new Coordinate(tx, ty);
                int borderX = getScreenWidth()/ 40;
                int borderY = getScreenHeight()/ 40 - 1;
                if(tx<0||ty<0||tx>=borderX||ty>=borderY){
                    flag=false;
                }
                //检查是否为目标终点
                if (nextStep.equals(target)) {
                    nextStep.nextPoint = point;
                    last = nextStep;
                    break;
                }
                //检查该坐标是否已经遍历了
                if(checkSet.contains(nextStep)){
                    flag=false;
                }

                if (flag) {
                    //通过数组，判断是否这一点可以走（2、3应当替换成对应的Obj的数字）
                    //数组越界 应当/40
                    if(GameMap.getMap()[tx][ty] != ObjType.air && GameMap.getMap()[tx][ty] != ObjType.surface){
                        flag=false;
                    }
                }
                //该点可以用
                if (flag) {
                    //将坐标纳入已经遍历的队列中
                    queue.offer(nextStep);
                    nextStep.nextPoint = point;
                    last = nextStep;
                }
                checkSet.add(nextStep);
                //重新选择方向遍历
                tx = point.getX();
                ty = point.getY();
            }
            //如果没有四个方向都遍历完就跳出（），说明已经找到了终点
            if (i != 4) {
                break;
            }
        }

        while (last!=null&&last.nextPoint!=null) {//避免加入起点，以免无法移动
            Coordinates.push(last);
            last = last.nextPoint;
        }
        return Coordinates;
    }
}
