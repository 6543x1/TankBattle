package entity;

import myEnum.Direction;
import myEnum.ObjType;
import panel.GamePanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;

import static panel.GamePanel.*;
import static panel.GamePanel.P1_TAG;
import static panel.GamePanel.executorService;

public class EnemyTank extends Tank {

    public EnemyTank(int x, int y, Direction direction, int id) {
        super(x, y, direction, id);
//        executorService.submit(new EnemyTankMove());
//        executorService.submit(new RandomMoveThread());
        stackFuture = executorService.submit(new TaskWithPath());//这一步很重要！！！
        executorService.submit(new TraceMove());
        //executorService.submit(new TaskWithPath());
    }


    class EnemyTankMove implements Runnable {
        @Override
        public void run() {
            //int[] ops={KeyEvent.VK_UP,KeyEvent.VK_DOWN,KeyEvent.VK_LEFT,Ke.yEvent.VK_RIGHT,KeyEvent.VK_NUMPAD0};
            while (alive) {
                Coordinate coordinate = new Coordinate(x / 40, y / 40);
                //System.out.println("Enemy Direction:"+GetDirection(coordinate,next));
                // GetMoveDirection(GetDirection(coordinate,next));
                GetMoveDirection(key);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + ":休眠中断");
                }
            }
        }
    }

    class RandomMoveThread implements Runnable {
        @Override
        public void run() {
            while (alive) {
                key = RandomMove();
                System.out.println("Enemy go: " + key);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + ":休眠中断");
                }
            }
        }

    }
    private Coordinate randomCoord() {
        Random random = new Random(System.currentTimeMillis());
        int x, y;
        do {
            y = random.nextInt(map.length);
            x = random.nextInt(map[0].length);
        } while (map[y][x]!= ObjType.air);
        return new Coordinate(x, y);
    }
    class TraceMove implements Runnable{
        @Override
        public void run(){
            Direction direction=curDirection;
            int count = 0;
            while (alive) {
                if (stackFuture.isDone()) {
                    try {
                        //使用栈存放广度遍历算法得到的移动的路径
                        Stack<Coordinate> result = stackFuture.get();
                        System.out.println("Path="+result);
                        System.out.println("Tank coordinate:"+x/40+","+y/40);
                        if(next!=null){
                        System.out.println(next);}
                        //获得下一个路径
                        if (null != result && result.size() != 0 && null == next) {
                            next = result.pop();
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
               // 很可能是广度遍历算法出事了
                //next=randomCoord();
                //TankMove();
                Coordinate coord=new Coordinate(x/40,y/40);
                if (null != next && !next.equals(coord)) {
                    if (Math.abs(coord.x - next.x) > 1 || Math.abs(coord.y - next.y) > 1) {
                        System.out.println(Thread.currentThread().getName() + ":" + coord.toString() + "->" + next.toString());
                    }
                    GetMoveDirection(GetDirection(coord, next));
                }
                //为了防止两个坦克为了竞争同一个前面的方块而卡住
                // 这里采用如果在同一个移动方向停滞过久就往反方向移动一格的方法
                if (direction == curDirection) {
                    if (++count > 30) {
                        if (!CheckAndCorrectPosition()) {
                            int n;
                            switch (direction) {
                                case UP:
                                    n = KeyEvent.VK_DOWN;
                                    break;
                                case DOWN:
                                    n = KeyEvent.VK_UP;
                                    break;
                                case LEFT:
                                    n = KeyEvent.VK_RIGHT;
                                    break;
                                case RIGHT:
                                    n = KeyEvent.VK_LEFT;
                                    break;
                                default:
                                    n = KeyEvent.VK_SHIFT;
                            }
                            try {
                                for (int j = 0; j < 5; ++j) {
                                    GetMoveDirection(n);
                                    Thread.sleep(200);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        count = 0;
                    }
                } else {
                    direction = curDirection;
                    count = 0;
                }
                try {
                    Thread.sleep(500);
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
        if (coord.x - next.x <= -1) {
            n = KeyEvent.VK_RIGHT;
        } else if (coord.x - next.x >= 1) {
            n = KeyEvent.VK_LEFT;
        } else {
            if (coord.y - next.y <= -1) {
                n = KeyEvent.VK_DOWN;
            } else {
                n = KeyEvent.VK_UP;
            }
        }
        return n;
    }

    private int RandomMove() {
        int[] ops = {KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_NUMPAD0};
        Random random = new Random(System.currentTimeMillis());
        return ops[random.nextInt(5)];
    }

    @Override
    public void draw(Graphics2D g2) {
        //血条和蓝条的高度
        //这个paintTank方法也应该写到坦克类中
        int h = 5;
        g2.drawImage(getImage(), getX(), getY(), width, height, null);

        g2.setColor(Color.RED);
        //玩家血条设置为绿色
        //打印血条
        if (y >= height) {
            g2.draw3DRect(getX(), getY() - 10, getFullHp(), h, true);
            g2.fill3DRect(getX(), getY() - 10, getHP(), h, true);
        } else {
            g2.draw3DRect(getX(), getY() + 40, getFullHp(), h, true);
            g2.fill3DRect(getX(), getY() + 40, getHP(), h, true);
        }
        g2.setColor(Color.WHITE);
        g2.drawString("(" + getX() + "," + getY() + ")", getX(), getY() + 1 + h + 55);
        g2.drawString("(" + getX() + "," + getY() + ")", getX(), getY() - 55);
    }

    @Override
    public void GetMoveDirection(int n) {
        int t_x = x/width;
        int t_y = y/height;
        super.GetMoveDirection(n);
        // 如果坐标发生了一整格的变化，就更新二维数组->方便寻路系统
        if (t_y != y/height || t_x != x/width) {
            //x、y本来就是*40后放入tank的
            int t_y2 = y / height;
            int t_x2 = x / width;
            //我并没有存储坦克的地图坐标。。。大意了，这样不好寻路？
            //因为移动之后要把原来的点给变成空气
            if (((t_y2 != t_y) || (t_x2 != t_x)) && (x % 40 == 0 || y % 40 == 0)) {
                //20%40!=0 !!!!!
//                synchronized (map) {
//                    map[t_y2][t_x2] = map[t_y][t_x];
//                    map[t_y][t_x] = ObjType.air;
//                }//运行到此处出现问题，死锁了？
                map[t_y2][t_x2] = map[t_y][t_x];
                map[t_y][t_x] = ObjType.air;
                // coord.x = t_x;
                // coord.y = t_y;
//                if (id == Game.PLAY_1) Game.printMap();

                if (!executorService.isShutdown()) {
                    stackFuture = executorService.submit(new TaskWithPath());
                }
                next = null;//很重要！


            }
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
        Coordinate target = new Coordinate(tanks.get(P1_TAG).getX()/width, tanks.get(P1_TAG).getY()/height);
        Queue<Coordinate> queue = new LinkedBlockingQueue<>();
        HashSet<Coordinate> checkSet=new HashSet<>();
        Coordinate coordinate = new Coordinate(x/width, y/height);
        int[] a= {2,3,4,5,6};
        int b=Arrays.stream(a).max().getAsInt();
        checkSet.add(coordinate);
        queue.offer(coordinate);//当前坐标入队，若入队失败抛出异常
        Coordinate last = null;
        boolean flag;
        while (!queue.isEmpty()) {
            Coordinate point = queue.poll();//队头出列
           // System.out.println("t:"+point);
//            　　抛出异常 　　返回特殊值
//            插入：add(e) 　　  offer(e)  插入一个元素
//            移除：remove()      poll()      移除和返回队列的头
//            检查：element()     peek()    返回但不移除队列的头。
            int tx = point.x;
            int ty = point.y;
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
                int borderX = getScreenWidth()/ 60;
                int borderY = getScreenHeight()/ 60 - 1;
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
                    flag = (map[ty][tx] == ObjType.air || map[ty][tx] == ObjType.surface);
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
                tx = point.x;
                ty = point.y;
            }
            //如果没有四个方向都遍历完就跳出（），说明已经找到了终点
            if (i != 4) {
                break;
            }
        }
        Stack<Coordinate> Coordinates = new Stack<>();
        while (last!=null&&last.nextPoint!=null) {//避免加入起点，以免无法移动
            Coordinates.push(last);
            last = last.nextPoint;
        }
        return Coordinates;
    }
}
