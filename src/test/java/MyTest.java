import entity.Coordinate;
import entity.Direction;
import entity.ObjType;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

import static panel.GamePanel.*;
import static panel.GamePanel.map;

public class MyTest {

    @Test
    public void testHashCode() {
        Coordinate a = new Coordinate(10, 20);
        Coordinate b = new Coordinate(10, 20);
        System.out.println(a.equals(b));
        System.out.println(a.hashCode());
        System.out.println(b.hashCode());
    }

    @Test
    public void BFSGetPath() {
        ObjType[][] map = new ObjType[14][22];//map大小 ：x=22.5 y=14
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 22; j++) {
                map[i][j] = ObjType.air;
            }
        }
        Coordinate target = new Coordinate(0, 0);
        map[0][0] = ObjType.playerTank;
        Queue<Coordinate> queue = new LinkedBlockingQueue<>();
        HashSet<Coordinate> isCheck = new HashSet<>();
        Coordinate thisCoordinate = new Coordinate(5, 3);
        map[5][3] = ObjType.enemyTank;
        isCheck.add(thisCoordinate);
        queue.offer(thisCoordinate);//当前坐标入队，若入队失败抛出异常
        Coordinate last = null;
        boolean flag = true;
        while (!queue.isEmpty()) {
            Coordinate point = queue.poll();//队头出列
            System.out.println("point:" + point);
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
                Coordinate z = new Coordinate(tx, ty);
                System.out.println("z:" + z);
                int borderX = getScreenWidth() / 40;
                int borderY = getScreenHeight() / 40 - 1;
                if (tx < 0 || ty < 0 || tx >= borderX || ty >= borderY) {
                    flag = false;
                }
                //检查是否为目标终点
                if (z.equals(target)) {
                    z.nextPoint = point;
                    last = z;
                    break;
                }
                //检查该坐标是否已经遍历了
                if (isCheck.contains(z)) {
                    flag = false;
                }
//                for (Coordinate c : IsMove) {//此处完全可以换成HashSet吗，或者hashMap
//                    if (c.equals(z)) {//这个判断有问题！
//                        //要重写equals方法！！！
//                        flag = false;
//                        break;
//                    }
//                }

                if (flag) {
                    //通过数组，判断是否这一点可以走（2、3应当替换成对应的Obj的数字）
                    //数组越界 应当/40
                    flag = (map[ty][tx] == ObjType.air || map[ty][tx] == ObjType.surface);
//                    if (flag) {
//                        Coordinate temp = new Coordinate(z.x, z.y);
//                        System.out.println("temp:"+temp);
//                        switch (direction) {
//                            case UP:
//                                temp.y -= 1;
//                                break;
//                            case LEFT:
//                                temp.x -= 1;
//                                break;
//                            case RIGHT:
//                                temp.x += 1;
//                                break;
//                            case DOWN:
//                                temp.y += 1;
//                                break;
//                        }
//                        if(!(temp.x<0||temp.y<0||temp.x>=borderX||temp.y>=borderY)){
//                            flag = (map[temp.y][temp.x] == ObjType.air || map[temp.y][temp.x] == ObjType.hitWall || map[temp.y][temp.x] == ObjType.playerTank || map[temp.y][temp.x] != ObjType.enemyTank);
//                        }
//                        else{
//                            flag=false;
//                        }
//                        //下一点是否被挤占空间
//                    }
                    //这一长串确实匪夷所思，直接导致寻路异常
                }
                //该点可以用
                if (flag) {
                    //将坐标纳入已经遍历的队列中
                    queue.offer(z);
                    z.nextPoint = point;
                    last = z;
                }
                isCheck.add(z);
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
        while (null != last && last.nextPoint != null) {
            Coordinates.push(last);
            last = last.nextPoint;
        }
        System.out.println("Trace is:");
        while (!Coordinates.isEmpty()) {
            System.out.println(Coordinates.pop());
        }
    }

    public Stack<Coordinate> GetPath2() {
        ObjType[][] map = new ObjType[14][22];//map大小 ：x=22.5 y=14
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 22; j++) {
                map[i][j] = ObjType.air;
            }
        }
        Coordinate target = new Coordinate(0, 0);
        Queue<Coordinate> d_q = new LinkedBlockingQueue<>();
        ArrayList<Coordinate> IsMove = new ArrayList<>();
        Coordinate coord = new Coordinate(5, 3);
        IsMove.add(coord);
        d_q.offer(coord);
        Coordinate last = null;
        boolean flag;
        while (!d_q.isEmpty()) {
            Coordinate t = d_q.poll();
            int tx = t.x;
            int ty = t.y;
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
                Coordinate z = new Coordinate(tx, ty);
                //检查是否为目标终点
                if (z.equals(target)) {
                    z.nextPoint = t;
                    last = z;
                    break;
                }
                //检查该坐标是否已经遍历了
                for (Coordinate c : IsMove) {
                    if (c.equals(z)) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    //通过数组，判断是否这一点可以走
                    flag = (map[ty][tx] == ObjType.air || map[ty][tx] == ObjType.wall);
                    if (flag) {
                        Coordinate temp = new Coordinate(z.x, z.y);
                        switch (direction) {
                            case UP:
                                temp.y -= 1;
                                break;
                            case LEFT:
                                temp.x -= 1;
                                break;
                            case RIGHT:
                                temp.x += 1;
                                break;
                            case DOWN:
                                temp.y += 1;
                                break;
                        }
                        flag = (map[temp.y][temp.x] == ObjType.air);
                    }
                }
                //该点可以用
                if (flag) {
                    //将坐标纳入已经遍历的队列中
                    d_q.offer(z);
                    z.nextPoint = t;
                    last = z;
                }
                IsMove.add(z);
                //重新选择方向遍历
                tx = t.x;
                ty = t.y;
            }
            //如果没有四个方向都遍历完就跳出，说明已经找到了终点
            if (i != 4) {
                break;
            }
        }
        Stack<Coordinate> coords = new Stack<>();
        while (null != last && last.nextPoint != null) {
            coords.push(last);
            last = last.nextPoint;
        }
        return coords;
    }

    @Test
    public void BFS() {//PASS 可能上面哪里写错了
        int[][] map = new int[14][22];
        for (int i = 0; i < 14; i++) {
            for (int j = 0; j < 22; j++) {
                map[i][j] = 0;
            }
        }
        map[0][0] = 1;
        map[5][3] = 2;
        Queue<Coordinate> queue = new LinkedBlockingQueue<>();
        HashSet<Coordinate> hashSet = new HashSet<>();
        Stack<Coordinate> stack = new Stack<>();
        Coordinate target = new Coordinate(0, 0);
        Coordinate mySelf = new Coordinate(5, 3);
        queue.offer(mySelf);
        hashSet.add(mySelf);
        Coordinate last = null;
        while (!queue.isEmpty()) {
            Coordinate point = queue.poll();
            Coordinate temp;
            for (int i = 0; i < 4; i++) {
                switch (i) {
                    case 0://向上
                        temp = new Coordinate(point.x, point.y - 1);
                        break;
                    case 1://向下
                        temp = new Coordinate(point.x, point.y + 1);
                        break;
                    case 2:
                        temp = new Coordinate(point.x - 1, point.y);
                        break;
                    case 3:
                        temp = new Coordinate(point.x + 1, point.y);
                        break;
                    default:
                        temp = new Coordinate(0, 0);
                }
                if (temp.x < 0 || temp.y < 0 || temp.x >= 14 || temp.y >= 22) {
                    continue;
                }
                if (temp.equals(mySelf)) {
                    continue;
                }
                if (temp.equals(target)) {
                    //记录路径似乎有点难
                    temp.nextPoint = point;
                    last = temp;
                    break;
                }
                if (!hashSet.contains(temp)) {
                    temp.nextPoint = point;
                    queue.offer(temp);
                    hashSet.add(temp);
                }
            }
        }
        while (last != null) {
            System.out.println(last);
            last = last.nextPoint;
        }
//        Coordinate{x=0, y=0)
//            Coordinate{x=0, y=1)
//                Coordinate{x=1, y=1)
//                    Coordinate{x=2, y=1)
//                        Coordinate{x=3, y=1)
//                            Coordinate{x=4, y=1)
//                                Coordinate{x=5, y=1)
//                                    Coordinate{x=5, y=2)
//                                        Coordinate{x=5, y=3)
    }

    public void test2() {
        Scanner input = new Scanner(System.in);
        int count = input.nextInt();
        int[] scores = new int[count];
        int sum = 0;
        for (int i = 0; i < count; i++) {
            scores[i] = input.nextInt();
        }
        for (int i = 0; i < count; i++) {
            sum += scores[i];
        }

        sum /= count;
        System.out.println(sum);
    }

    @Test
    public void testPlusTo100() {
        int n = 0;
        int sum = 0;
        while (n < 100) {
            n++;
            sum += n;
        }
        System.out.println(sum);
    }

    @Test
    public void BubbleTest() {
        //int[] ns = {28, 12, 89, 73, 65, 18, 96, 50, 8, 36};
        //int[] ns={9,8,7,6,5,4,3,2,1,0};
        int[] ns={0,1,2,9,4,5,6,6,7,8};
        // 排序前:
        boolean isSwap = false;
        //System.out.println(Arrays.toString(ns));
        for (int i = 0; i < ns.length - 1; i++) {
            System.out.println("i="+i+","+"j<"+(ns.length-i-1));
            for (int j = 0; j < ns.length - i - 1; j++) {
                if (ns[j] > ns[j + 1]) {
                    // 交换ns[j]和ns[j+1]:
                    isSwap = true;
                    int tmp = ns[j];
                    ns[j] = ns[j + 1];
                    ns[j + 1] = tmp;
                }
            }
//          z
            isSwap=false;
            System.out.println(Arrays.toString(ns));
        }
        // 排序后:
      //  System.out.println(Arrays.toString(ns));
    }


}
