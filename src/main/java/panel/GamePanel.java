package panel;

import entity.*;
import myEnum.Direction;
import myEnum.Mode;
import myEnum.ObjType;
import utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.awt.event.KeyEvent.VK_R;

public class GamePanel extends JPanel {
    public static ExecutorService executorService = Executors.newCachedThreadPool();
    private Image OffScreenImage;
    private JFrame play;
    //坦克的移动区域
    private final static int screenWidth = 700;
    private final static int screenHeight = 600;
    private int level;
    //坦克的移动
    public static int P1_TAG;
    public static int P2_TAG;
    public static Mode mode;
    public static AtomicBoolean live = new AtomicBoolean(false);//游戏面板是否存活

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    private Coordinate randomCoord() {
        Random random = new Random(System.currentTimeMillis());
        int x, y;
        //do {
        y = random.nextInt(GameMap.map.length);
        x = random.nextInt(GameMap.map[0].length);
        // } while (map[y][x] != BLANK);
        return new Coordinate(x, y);
    }

    /**
     * 初始化地图
     */
    private void initMap() {

        int x = screenWidth / 40;
        int y = screenHeight / 40 - 1;

        GameMap.map = new ObjType[x + 1][y + 1];//map大小 ：x=22.5 y=14
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                GameMap.map[i][j] = ObjType.air;
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

    private void initTank() {
        Coordinate coord = randomCoord();
//        while (map[coord.y][coord.x]==WALL){
//            coord=randomCoord();
//        }
//        PlayerTank p1 = new PlayerTank(coord.x*40,coord.y*40, Direction.UP, 1);
        PlayerTank p1 = new PlayerTank(0, 0, Direction.UP, 1);
        p1.setImage(ImageUtils.p1upImage);
        p1.setSpeed(20);
        P1_TAG = p1.getId();
        //GameMap.map[coord.getX()][coord.getY()] = ObjType.playerTank;
        GameMap.tanks.put(p1.getId(), p1);
        coord = randomCoord();
//        EnemyTank e1=new EnemyTank(coord.x*40,coord.y*40,Direction.UP,11);
        EnemyTank e1 = new EnemyTank(5 * 40, 3 * 40, Direction.UP, 11);
        e1.setImage(ImageUtils.enemy1upImage);
        e1.setSpeed(40);//速度尽量设置成40的倍数，以免移动到比较特别的点导致寻路系统失效
        //GameMap.map[coord.getY()][coord.getX()]=ObjType.enemyTank;
        GameMap.tanks.put(e1.getId(), e1);
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

    private void initWall() {

        // Coordinate coord = randomCoord();
        Coordinate coord = new Coordinate(10, 6);
//        while (map[coord.y][coord.x]==WALL){
//            coord=randomCoord();
//        }
        GameMap.map[coord.getX()][coord.getY()] = ObjType.hitWall;
        BrickWall brickWall = new BrickWall(coord.hashCode(), coord.getX() * 40, coord.getY() * 40, 40, 40);
        coord.setX(coord.getX() + 2);
        coord.setY(coord.getY() + 2);
        SteelWall steelWall = new SteelWall(coord.hashCode(), coord.getX() * 40, coord.getY() * 40, 40, 40);
        GameMap.map[coord.getX()][coord.getY()] = ObjType.wall;
        GameMap.walls.put(brickWall.getId(), brickWall);
        GameMap.walls.put(steelWall.getId(), steelWall);
        coord.setX(280);
        coord.setY(560);
        Base base = new Base(-1, coord.getX(), coord.getY(), 55, 40);
        GameMap.walls.put(base.getId(), base);//这样让基地的血量可以被打印出来
        GameMap.map[coord.getX() / 40][coord.getY() / 40] = ObjType.hitWall;
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
                GameMap.map[wall.getX()/40][wall.getY()/40] = ObjType.hitWall;
                GameMap.walls.put(wall.getId(), wall);
            }
        }
        Random random = new Random(System.currentTimeMillis());
        ObjType objType = ObjType.air;

        int n = random.nextInt(100);
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
                        wall = new BrickWall(coord.hashCode(), coord.getX() * 40, coord.getY() * 40, 40, 40);
                        GameMap.walls.put(wall.getId(), wall);
                        GameMap.map[i][j] = ObjType.hitWall;
                        System.out.printf("Point %d,%d is Wall\n", i, j);
                        System.out.println(GameMap.map[i][j]);
                    } else if (n <= 15) {
                        wall = new SteelWall(coord.hashCode(), coord.getX() * 40, coord.getY() * 40, 40, 40);
                        GameMap.walls.put(wall.getId(), wall);
                        GameMap.map[i][j] = ObjType.wall;
                        System.out.printf("Point %d,%d is Wall\n", i, j);
                        System.out.println(GameMap.map[i][j]);
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
    private void initSurface(){
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

    public GamePanel(Mode mode, int level, JFrame play) {
        this.level = level;
        this.play = play;
        setForeground(Color.WHITE);
        setBackground(Color.BLACK);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((d.width - screenWidth) / 2, (d.height - screenHeight) / 2, screenWidth, screenHeight);
        setSize(620, 640);//Panel大小实际上会超出窗口大小......
        setLayout(null);
        GamePanel.mode = mode;
        initMap();
        initWall();
        initSurface();
        initTank();
        addKeyListener(new KeyBoardListener());
        //executorService.submit(new MissileMove());
        executorService.submit(new Draw());
    }


    /**
     * 重绘函数
     */
    @Override
    synchronized public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;

        //绘画墙体
        for (Wall wall : GameMap.walls.values()) {
            wall.draw(g2);
        }

        for(Surface surface:GameMap.surfaces.values()){
            surface.draw(g2);
        }

        //绘制坦克
        for (Tank tank : GameMap.tanks.values()) {
            tank.draw(g2);
        }

        //子弹绘画
        for (Shell shell : GameMap.shells.values()) {
            shell.draw(g2);
        }


    }

    //缓存绘图
    @Override
    synchronized public void update(Graphics g) {
        super.update(g);
        if (OffScreenImage == null) {
            OffScreenImage = this.createImage(screenWidth, screenHeight);
        }
        Graphics goffscrenn = OffScreenImage.getGraphics();    //设置一个内存画笔颜色为前景图片颜色
        Color c = goffscrenn.getColor();    //还是先保存前景颜色
        goffscrenn.setColor(Color.BLACK);    //设置内存画笔颜色为绿色
        goffscrenn.fillRect(0, 0, screenWidth, screenHeight);    //画成图片，大小为游戏大小
        goffscrenn.setColor(c);    //还原颜色
        g.drawImage(OffScreenImage, 0, 0, null);    //在界面画出保存的图片
        paint(goffscrenn);    //把内存画笔调用给paint
    }

    /**
     * 图像重绘线程
     */
    class Draw implements Runnable {
        @Override
        public void run() {
            while (live.get()) {
                repaint();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 监听按键
     */
    private class KeyBoardListener extends KeyAdapter {


        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            //区分两种不同的按键
            //ASDWG为P2的按键
            //上下左右+小键盘0为P1按键
            if (key == KeyEvent.VK_N) {
                nextLevel();
            }
            if (key < 65) {
                if (key != KeyEvent.VK_SHIFT && GameMap.tanks.get(P1_TAG) != null) {
                    PlayerTank p1 = (PlayerTank) GameMap.tanks.get(P1_TAG);
                    p1.setKey(key);
                    p1.setMove(true);
                }
                if (key == KeyEvent.VK_ESCAPE) {
                    ShutDown();
                }
            } else {
                if (key != KeyEvent.VK_G && GameMap.tanks.get(P2_TAG) != null) {
                    switch (key) {
                        case KeyEvent.VK_W:
                            key = KeyEvent.VK_UP;
                            break;
                        case KeyEvent.VK_A:
                            key = KeyEvent.VK_LEFT;
                            break;
                        case KeyEvent.VK_S:
                            key = KeyEvent.VK_DOWN;
                            break;
                        case KeyEvent.VK_D:
                            key = KeyEvent.VK_RIGHT;
                            break;
                    }
                    PlayerTank p2 = (PlayerTank) GameMap.tanks.get(P2_TAG);
                    p2.setKey(key);
                    p2.setMove(true);
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            super.keyReleased(e);
            int key = e.getKeyCode();
            if (key < 65) {
                checkNextMove(key, GameMap.tanks.get(P1_TAG), null, P1_TAG);
            } else if (key == KeyEvent.VK_NUMPAD0) {
                PlayerTank p1 = (PlayerTank) GameMap.tanks.get(P1_TAG);
                if (GameMap.shells.get(P1_TAG) != null) {
                    System.out.println("当前仍有子弹在飞行！射击失败");
                    return;
                }
                p1.GetMoveDirection(key);
            } else {
                switch (key) {
                    case KeyEvent.VK_W:
                        key = KeyEvent.VK_UP;
                        break;
                    case KeyEvent.VK_A:
                        key = KeyEvent.VK_LEFT;
                        break;
                    case KeyEvent.VK_S:
                        key = KeyEvent.VK_DOWN;
                        break;
                    case KeyEvent.VK_D:
                        key = KeyEvent.VK_RIGHT;
                        break;
                    case KeyEvent.VK_G:
                        key = KeyEvent.VK_SHIFT;
                        break;
                }
                checkNextMove(key, null, GameMap.tanks.get(P2_TAG), P2_TAG);
            }
        }

        private void checkNextMove(int key, Tank tank, Tank o, int p1Tag) {
            if (tank != o) {
                if (key != KeyEvent.VK_SHIFT && key == GameMap.tanks.get(p1Tag).getKey()) {
                    PlayerTank p1 = (PlayerTank) GameMap.tanks.get(p1Tag);
                    p1.setMove(false);//若true则连续移动
                } else {
                    PlayerTank p1 = (PlayerTank) GameMap.tanks.get(p1Tag);
                    p1.GetMoveDirection(key);

                }
            }
        }
    }

    public static void ShutDown() {
        GamePanel.live.getAndSet(false);
        //停止所有的线程
        for (Tank tank : GameMap.tanks.values()) {
            tank.setAlive(false);
        }
        executorService.shutdown();
        executorService = Executors.newCachedThreadPool();
        GameMap.tanks.clear();
        GameMap.walls.clear();
        GameMap.shells.clear();

    }

    private void nextLevel() {
        level++;
        play.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(false);
        LevelPanel levelPanel = new LevelPanel(level, play, mode);
        play.setBounds(levelPanel.getBounds());
        play.setContentPane(levelPanel);
        play.setVisible(true);
        play.setResizable(false);
        levelPanel.requestFocus();
    }
}
