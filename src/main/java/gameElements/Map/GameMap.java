package gameElements.Map;

import gameElements.Coordinate;
import gameElements.Shell.Shell;
import gameElements.Surface.Grass;
import gameElements.Surface.Surface;
import gameElements.Tank.*;
import gameElements.Wall.*;
import myEnum.Difficulty;
import myEnum.Direction;
import myEnum.Mode;
import myEnum.ObjType;
import panel.GamePanel;
import utils.ImageUtils;
import utils.SettingsUtils;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class GameMap {
    private volatile static ObjType[][] map;//按比例尺1：40缩小的地图
    private static final ConcurrentHashMap<Integer, Tank> tanks = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, Shell> shells = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, Wall> walls = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, Surface> surfaces = new ConcurrentHashMap<>();
    private static int player1Lives = 0;
    private static int player2Lives = 0;
    private static int lightLives = 0;
    private static int normalLives = 0;
    private static int heavyLives = 0;
    private static final Coordinate ec1 = new Coordinate(160, 0);
    private static final Coordinate ec2 = new Coordinate(360, 0);
    private static final Coordinate ec3 = new Coordinate(560, 0);
    private static final Coordinate[] ecs = {ec1, ec2, ec3};
    private static int enemyNormalSpeed = 20;
    private static int enemyHeavySpeed = 10;
    private static int enemyLightSpeed = 30;
    private static int born=0;

    public static int getBaseX() {
        return baseX;
    }


    public static int getBaseY() {
        return baseY;
    }


    private static final int baseX=320;
    private static final int baseY=560;


    private static Difficulty difficulty;


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
        setDifficulty(SettingsUtils.readGameSettings().getDifficulty());

    }

    public static void initSurface() {
        Coordinate coord = randomCoord();
        Random random = new Random(System.currentTimeMillis());
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
                    if (n <= 10) {
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

    public static void initTank(Mode mode,int level) {
        PlayerTank p1 = new PlayerTank(160, 560, Direction.UP, 1);
        PlayerTank p2;
        p1.setImage(ImageUtils.getP1upImage());
        p1.setSpeed(20);
        player1Lives = 3;
        GamePanel.setP1Tag(p1.getId());
        tanks.put(p1.getId(), p1);
        if (mode == Mode.Double) {
            p2 = new PlayerBotTank(560, 560, Direction.UP, 2);
            p2.setImage(ImageUtils.getP2upImage());
            p2.setSpeed(20);
            GamePanel.setP2Tag(p2.getId());
            tanks.put(p2.getId(), p2);
            player2Lives = 3;
        }
        EnemyTank e1 = new EnemyTank(160, 0, Direction.DOWN, 11);
        e1.setImage(ImageUtils.getEnemy1downImage());
        e1.setSpeed(enemyNormalSpeed);//速度尽量设置成40的倍数，以免移动到比较特别的点导致寻路系统失效
        //GameMap.map[coord.getY()][coord.getX()]=ObjType.enemyTank;
        tanks.put(e1.getId(), e1);
        e1 = new EnemyTank(360, 0, Direction.DOWN, 12);
        e1.setImage(ImageUtils.getEnemy1downImage());
        e1.setSpeed(enemyNormalSpeed);//速度尽量设置成40的倍数，以免移动到比较特别的点导致寻路系统失效
        tanks.put(e1.getId(), e1);
        e1 = new EnemyTank(560, 0, Direction.DOWN, 13);
        e1.setImage(ImageUtils.getEnemy1downImage());
        e1.setSpeed(enemyNormalSpeed);//速度尽量设置成40的倍数，以免移动到比较特别的点导致寻路系统失效
        tanks.put(e1.getId(), e1);
        decideEnemyLives(level);

    }
    private static void decideEnemyLives(int level){
        normalLives = 15;
        lightLives=4;
        heavyLives=1;//第一关
        heavyLives+=(level-1);

    }

    private static Coordinate randomCoord() {
        Random random = new Random(System.currentTimeMillis());
        int x, y;
        y = random.nextInt(GameMap.map.length);
        x = random.nextInt(GameMap.map[0].length);
        return new Coordinate(x, y);
    }

    public static void initWall() {
        Coordinate coord = new Coordinate(10, 6);
        map[coord.getX()][coord.getY()] = ObjType.hitWall;
        BrickWall brickWall = new BrickWall(coord.hashCode(), coord.getX() * 40, coord.getY() * 40, 40, 40);
        coord.setX(coord.getX() + 2);
        coord.setY(coord.getY() + 2);
        SteelWall steelWall = new SteelWall(coord.hashCode(), coord.getX() * 40, coord.getY() * 40, 40, 40);
        map[coord.getX()][coord.getY()] = ObjType.wall;
        walls.put(brickWall.getId(), brickWall);
        walls.put(steelWall.getId(), steelWall);
        Base base = new Base(-1, baseX, baseY, 55, 40);
        walls.put(base.getId(), base);//这样让基地的血量可以被打印出来
        map[coord.getX() / 40][coord.getY() / 40] = ObjType.hitWall;
        int count = 1;
        Wall wall = null;
        for (int i = base.getX() - 20; i <= base.getX() + base.getWidth() + 20; i += 20) {
            for (int j = base.getY() - 20; j < base.getY() + base.getHeight(); j += 20) {
                wall = new HalfBrickWall(count++, i, j, 20, 20);
                if (wall.isCollided(base)) {
                    continue;
                }
//                System.out.printf("Half Wall %d,%d\n", wall.getX(), wall.getY());
                map[wall.getX() / 40][wall.getY() / 40] = ObjType.hitWall;
                walls.put(wall.getId(), wall);
            }
        }
        Random random = new Random(System.currentTimeMillis());
        int n;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if (j == 0) {
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
//                        System.out.printf("Point %d,%d is Wall\n", i, j);
                        System.out.println(map[i][j]);
                    } else if (n <= 15) {
                        wall = new SteelWall(coord.hashCode(), coord.getX() * 40, coord.getY() * 40, 40, 40);
                        walls.put(wall.getId(), wall);
                        map[i][j] = ObjType.wall;
//                        System.out.printf("Point %d,%d is Wall\n", i, j);
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




    }

    public static void reBornPlayer(int tag) {
        if (tag == GamePanel.getP1Tag()) {
            if (player1Lives > 0) {
                PlayerTank p1 = new PlayerTank(160, 560, Direction.UP, GamePanel.getP1Tag());
                p1.setImage(ImageUtils.getP1downImage());
                p1.setSpeed(20);
                tanks.put(p1.getId(), p1);
                player1Lives--;
            }
        } else if (tag == GamePanel.getP2Tag()) {
            if (player2Lives > 0) {
                PlayerTank p2 = new PlayerBotTank(520, 560, Direction.UP, GamePanel.getP2Tag());
                p2.setImage(ImageUtils.getP1downImage());
                p2.setSpeed(20);
                tanks.put(p2.getId(), p2);
                player2Lives--;
            }
        }
        if (player1Lives == 0 && player2Lives == 0) {
            GamePanel.reStart();
        }
    }

    public static void reBornEnemy() {
        Tank e1;
        if (lightLives != 0) {
            e1 = new EnemyLightTank(ecs[born].getX(), ecs[born].getY(), Direction.DOWN, ecs[born].hashCode() + lightLives);
            e1.setImage(ImageUtils.getLightDownImage());
            e1.setSpeed(enemyLightSpeed);//速度尽量设置成40的倍数，以免移动到比较特别的点导致寻路系统失效
            GameMap.tanks.put(e1.getId(), e1);//注意：坦克的id必须唯一标识自己，便于移除00
            lightLives--;
            System.out.println("敌人死亡,light");
        } else if (normalLives!=0) {
            e1 = new EnemyTank(ecs[born].getX(), ecs[born].getY(), Direction.DOWN, ecs[born].hashCode() + normalLives);
            e1.setImage(ImageUtils.getEnemy1downImage());
            e1.setSpeed(enemyNormalSpeed);//速度尽量设置成40的倍数，以免移动到比较特别的点导致寻路系统失效
            GameMap.tanks.put(e1.getId(), e1);//注意：坦克的id必须唯一标识自己，便于移除00
            normalLives--;
            System.out.println("敌人死亡,normal");
        } else if (heavyLives!=0) {
            e1 = new EnemyHeavyTank(ecs[born].getX(), ecs[born].getY(), Direction.DOWN, ecs[born].hashCode() + heavyLives);
            e1.setImage(ImageUtils.getHeavyDownImage());
            e1.setSpeed(enemyHeavySpeed);//速度尽量设置成40的倍数，以免移动到比较特别的点导致寻路系统失效
            GameMap.tanks.put(e1.getId(), e1);//注意：坦克的id必须唯一标识自己，便于移除00
            heavyLives--;
            System.out.println("敌人死亡,heavy");
        }
        born=(born+1)%3;
        checkIsWin();


    }

    private static void checkIsWin() {
        if (GamePanel.getMode() == Mode.Single) {
            if (tanks.size() == 1 && tanks.containsKey(GamePanel.getP1Tag())) {
                GamePanel.win();
            }
        }
        else{
            if(tanks.size()==2){//二人存活
                if(tanks.containsKey(GamePanel.getP1Tag())&&tanks.containsKey(GamePanel.getP2Tag())){
                    GamePanel.win();
                }
            }
            if(tanks.size()==1){//一人存活
                if(tanks.containsKey(GamePanel.getP1Tag())||tanks.containsKey(GamePanel.getP2Tag())){
                    GamePanel.win();
                }
            }
        }
    }



    public static void setDifficulty(Difficulty difficulty) {
        GameMap.difficulty = difficulty;
        if(difficulty==Difficulty.normal){
            enemyNormalSpeed=25;
            enemyLightSpeed=35;
            enemyHeavySpeed=15;
        }
        if(difficulty==Difficulty.hard){
            enemyNormalSpeed=30;
            enemyLightSpeed=40;
            enemyHeavySpeed=20;
        }
    }


    public static ObjType[][] getMap() {
        return map;
    }



    public static ConcurrentHashMap<Integer, Tank> getTanks() {
        return tanks;
    }



    public static ConcurrentHashMap<Integer, Shell> getShells() {
        return shells;
    }



    public static ConcurrentHashMap<Integer, Wall> getWalls() {
        return walls;
    }



    public static ConcurrentHashMap<Integer, Surface> getSurfaces() {
        return surfaces;
    }


}
