package panel;

import entity.*;
import myEnum.Mode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class GamePanel extends JPanel {
    public static ExecutorService executorService = Executors.newCachedThreadPool();
    private Image OffScreenImage;
    private JFrame play;
    //坦克的移动区域
    public final static int screenWidth = 720;
    public final static int screenHeight = 600;
    private int level;
    private static int P1_TAG;
    private static int P2_TAG;

    public static Mode mode;
    public static AtomicBoolean live = new AtomicBoolean(false);//游戏面板是否存活

    public static GamePanel getGamePanel() {
        return gamePanel;
    }


    private static GamePanel gamePanel;

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }
    public static int getP1Tag(){
        return P1_TAG;
    }
    public static int getP2Tag(){
        return P2_TAG;
    }

    public static void setP1Tag(int p1Tag) {
        P1_TAG = p1Tag;
    }

    public static void setP2Tag(int p2Tag) {
        P2_TAG = p2Tag;
    }

    public static GamePanel newGamePanel(Mode mode, int level, JFrame play){
        if(gamePanel==null){
            gamePanel=new GamePanel(mode,level,play);
        }
        return gamePanel;
    }
    private GamePanel(Mode mode, int level, JFrame play) {
        this.level = level;
        this.play = play;
        setForeground(Color.WHITE);
        setBackground(Color.BLACK);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((d.width - screenWidth) / 2, (d.height - screenHeight) / 2, screenWidth, screenHeight);
        setSize(620, 640);//Panel大小实际上会超出窗口大小......
        setLayout(null);
        GamePanel.mode = mode;
        GameMap.initMap();
        GameMap.initWall();
        GameMap.initSurface();
        GameMap.initTank(mode);
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
        //绘制坦克
        for (Tank tank : GameMap.tanks.values()) {
            tank.draw(g2);
        }
        //绘画墙体
        for (Wall wall : GameMap.walls.values()) {
            wall.draw(g2);
        }

        for(Surface surface:GameMap.surfaces.values()){
            surface.draw(g2);
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
        gamePanel=null;
    }
    public static void reStart() {
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
        GamePanel backup=gamePanel;
        gamePanel=null;
        backup.thisLevel();

    }

    private void nextLevel() {
        gamePanel=null;
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

    private void thisLevel() {
        play.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //setVisible(false);
        LevelPanel levelPanel = new LevelPanel(level, play, mode);
        play.setBounds(levelPanel.getBounds());
        play.setContentPane(levelPanel);
        play.setVisible(true);
        play.setResizable(false);
        levelPanel.requestFocus();
    }
}
