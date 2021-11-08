package entity;

import myEnum.Direction;
import myEnum.Mode;
import myEnum.ObjType;
import panel.GamePanel;
import utils.ImageUtils;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class GameMap {
    //地图，储存除了子弹以外的东西
    //注意当使用Coord的x和y的时候是map[y][x]
    public volatile static ObjType[][] map;
    public volatile static ConcurrentHashMap<Integer, Tank> tanks = new ConcurrentHashMap<>();
    public volatile static ConcurrentHashMap<Integer,Shell> shells=new ConcurrentHashMap<>();
    public volatile static ConcurrentHashMap<Integer,Wall> walls=new ConcurrentHashMap<>();
    public volatile static ConcurrentHashMap<Integer,Surface> surfaces=new ConcurrentHashMap<>();
    private static int player1Lives=1;
    private static int player2Lives=1;
    private static int lightLives=0;
    private static int normalLives=0;
    private static int heavyLives=0;


    /**
     * 初始化地图
     */
    public static void initMap() {

        int x = GamePanel.screenWidth / 40;
        int y = GamePanel.screenHeight / 40 - 1;

        map = new ObjType[x + 1][y + 1];//map大小 ：x=22.5 y=14
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                map[i][j] = ObjType.air;
            }
        }
//
//        for (int i = 0; i < y; ++i) {
//            for (int j = 0; j < x; ++j) {
//                if (i == 0 || i == y - 1 || j == 0 || j == x - 1) {
//                    map[i][j] = STEELS;
//                    Wall wall = new Wall(new Coord(j, i), STEEL);
//                    walls.put(wall.hashCode(), wall);
//                } else {
//                    map[i][j] = BLANK;
//                }
//            }
//        }
//        //以下方法会导致地图生成过多的墙
//        //随机
//        for (int i = 0; i < x * y / 2; ++i) {
//            //Coord的y对应数组的行
//            Coord c = randomCoord();
//            Random random=new Random();
////            if(tanks.get(12).coord.x==c.x&&tanks.get(12).coord.y==y){
////                continue;//不要把砖块和坦克生成在一个坐标里！
////            }
//            if(random.nextInt(10)>8){//1/4概率生成墙
//                map[c.y][c.x] = WALLS;
//
//                Wall wall = new Wall(c, WALL);
//                walls.put(wall.hashCode(), wall);
//            }
//        }

    }
    public static void initSurface(){
        Coordinate coord=randomCoord();
        Random random=new Random(System.currentTimeMillis());
        int n;
        Surface surface;
        for (int i = 0; i < GameMap.map.length; i++) {
            for (int j = 0; j < GameMap.map[i].length; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if (i == 5 && j == 3) {
                    continue;
                }
                if (GameMap.map[i][j] == ObjType.air) {
                    n = random.nextInt(100);
                    coord.setX(i);
                    coord.setY(j);
                    if (n <= 15) {
                        surface = new Grass(coord.getX() * 40, coord.getY() * 40, 40, 40);
                        GameMap.surfaces.put(coord.hashCode(), surface);
                        GameMap.map[i][j] = ObjType.surface;
                        System.out.printf("Point %d,%d is grass\n", i, j);
                        System.out.println(GameMap.map[i][j]);
                    }

                }

            }
        }
    }

    public static void initTank(Mode mode) {
        Coordinate coord =randomCoord();
//        while (map[coord.y][coord.x]==WALL){
//            coord=randomCoord();
//        }
//        PlayerTank p1 = new PlayerTank(coord.x*40,coord.y*40, Direction.UP, 1);
        PlayerTank p1 = new PlayerTank(160, 560, Direction.UP, 1);
        p1.setImage(ImageUtils.p1upImage);
        p1.setSpeed(20);
        player1Lives=1;
        player2Lives=1;
        GamePanel.setP1Tag(p1.getId());
        //GameMap.map[coord.getX()][coord.getY()] = ObjType.playerTank;
        tanks.put(p1.getId(), p1);
        coord = randomCoord();
//        EnemyTank e1=new EnemyTank(coord.x*40,coord.y*40,Direction.UP,11);
        EnemyTank e1 = new EnemyTank(160, 0, Direction.DOWN, 11);
        e1.setImage(ImageUtils.enemy1downImage);
        e1.setSpeed(40);//速度尽量设置成40的倍数，以免移动到比较特别的点导致寻路系统失效
        //GameMap.map[coord.getY()][coord.getX()]=ObjType.enemyTank;
        tanks.put(e1.getId(), e1);
        e1=new EnemyTank(360,0,Direction.DOWN,12);
        e1.setImage(ImageUtils.enemy1downImage);
        e1.setSpeed(40);//速度尽量设置成40的倍数，以免移动到比较特别的点导致寻路系统失效
        tanks.put(e1.getId(),e1);
        e1=new EnemyTank(560,0,Direction.DOWN,13);
        e1.setImage(ImageUtils.enemy1downImage);
        e1.setSpeed(40);//速度尽量设置成40的倍数，以免移动到比较特别的点导致寻路系统失效
        tanks.put(e1.getId(),e1);
        //出生点1：120，0 360，0 720，0
        //双人模式
//        if (mode == Mode.Double) {
//            coord = randomCoord();
//            while (map[coord.y][coord.x]==WALL){
//                coord=randomCoord();
//            }
//            Tank p2 = new Tank(coord, DOWN, PLAY_2);
//            p2.speed = 10;
//            P2_TAG = p2.id;
//            map[coord.y][coord.x] = p2.id;
//            tanks.put(p2.id, p2);
//        } else if (mode == Mode.Single) {
//            init_ETank();
//        }
    }
    private static Coordinate randomCoord() {
        Random random = new Random(System.currentTimeMillis());
        int x, y;
        //do {
        y = random.nextInt(GameMap.map.length);
        x = random.nextInt(GameMap.map[0].length);
        // } while (map[y][x] != BLANK);
        return new Coordinate(x, y);
    }

    public static void initWall() {

        // Coordinate coord = randomCoord();
        Coordinate coord = new Coordinate(10, 6);
//        while (map[coord.y][coord.x]==WALL){
//            coord=randomCoord();
//        }
        map[coord.getX()][coord.getY()] = ObjType.hitWall;
        BrickWall brickWall = new BrickWall(coord.hashCode(), coord.getX() * 40, coord.getY() * 40, 40, 40);
        coord.setX(coord.getX() + 2);
        coord.setY(coord.getY() + 2);
        SteelWall steelWall = new SteelWall(coord.hashCode(), coord.getX() * 40, coord.getY() * 40, 40, 40);
        map[coord.getX()][coord.getY()] = ObjType.wall;
        walls.put(brickWall.getId(), brickWall);
        walls.put(steelWall.getId(), steelWall);
        coord.setX(320);
        coord.setY(560);
        Base base = new Base(-1, coord.getX(), coord.getY(), 55, 40);
        walls.put(base.getId(), base);//这样让基地的血量可以被打印出来
        map[coord.getX() / 40][coord.getY() / 40] = ObjType.hitWall;
        int count = 1;
        Wall wall = null;
        for (int i = base.getX() - 20; i <= base.getX() + base.getWidth()+20; i += 20) {
            for (int j = base.getY() - 20; j < base.getY()+base.getHeight(); j += 20) {

//                if (Math.abs(i-base.getX())<40&&Math.abs(j-base.getY())<40) {
//                    continue;
//                }
                wall = new HalfBrickWall(count++, i, j, 20, 20);
                if(wall.isCollided(base)){
                    continue;
                }
                System.out.printf("Half Wall %d,%d\n",wall.getX(),wall.getY());
                map[wall.getX()/40][wall.getY()/40] = ObjType.hitWall;
                walls.put(wall.getId(), wall);
            }
        }
        Random random = new Random(System.currentTimeMillis());
        ObjType objType = ObjType.air;

        int n = random.nextInt(100);
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if (i == 5 && j == 3) {
                    continue;
                }
                if (map[i][j] == ObjType.air) {
                    n = random.nextInt(100);
                    coord.setX(i);
                    coord.setY(j);
                    if (n <= 10) {
                        wall = new BrickWall(coord.hashCode(), coord.getX() * 40, coord.getY() * 40, 40, 40);
                        walls.put(wall.getId(), wall);
                        map[i][j] = ObjType.hitWall;
                        System.out.printf("Point %d,%d is Wall\n", i, j);
                        System.out.println(map[i][j]);
                    } else if (n <= 15) {
                        wall = new SteelWall(coord.hashCode(), coord.getX() * 40, coord.getY() * 40, 40, 40);
                        walls.put(wall.getId(), wall);
                        map[i][j] = ObjType.wall;
                        System.out.printf("Point %d,%d is Wall\n", i, j);
                        System.out.println(map[i][j]);
                    }

                }

            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //双人模式
//        if (mode == Mode.Double) {
//            coord = randomCoord();
//            while (map[coord.y][coord.x]==WALL){
//                coord=randomCoord();
//            }
//            Tank p2 = new Tank(coord, DOWN, PLAY_2);
//            p2.speed = 10;
//            P2_TAG = p2.id;
//            map[coord.y][coord.x] = p2.id;
//            tanks.put(p2.id, p2);
//        } else if (mode == Mode.Single) {
//            init_ETank();
//        }

    }
    public static void reBornPlayer(int tag){
        if(tag==GamePanel.getP1Tag()){
            if(player1Lives>0){
            PlayerTank p1 = new PlayerTank(160, 560, Direction.UP, GamePanel.getP1Tag());
            p1.setImage(ImageUtils.p1downImage);
            p1.setSpeed(20);
            tanks.put(p1.getId(),p1);
            player1Lives--;
            }
        }
        else if(tag==GamePanel.getP2Tag()){
            if(player2Lives>0){
            PlayerTank p1 = new PlayerTank(520, 560, Direction.UP, GamePanel.getP1Tag());
            p1.setImage(ImageUtils.p1downImage);
            p1.setSpeed(20);
            tanks.put(p1.getId(),p1);
            player2Lives--;
            }
        }
        if(player1Lives==0||player2Lives==0){
            GamePanel.reStart();
        }
    }
    public static void reBornEnemy(int tag){
        if(tag==GamePanel.getP1Tag()){

        }
        else if(tag==GamePanel.getP2Tag()){

        }
        if(lightLives==0&&normalLives==0&&heavyLives==0){
            //win
            //nextGeneration
        }
    }
    public static void checkNum(){
        if(tanks.size()==0){
            GamePanel.live.getAndSet(false);
        }
    }


}
