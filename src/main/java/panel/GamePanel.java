package panel;

import entity.*;
import entity.Map.GameMap;
import entity.Shell.Shell;
import entity.Surface.Surface;
import entity.Tank.PlayerTank;
import entity.Tank.Tank;
import entity.Wall.Wall;
import myEnum.Mode;
import utils.RankUtils;
import utils.SettingsUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class GamePanel extends JPanel {
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    private Image OffScreenImage;
    private JFrame play;
    //坦克的移动区域
    public final static int screenWidth = 720;
    public final static int screenHeight = 600;
    private int level;
    private static int P1_TAG;
    private static int P2_TAG;

    private static Mode mode;
    private static AtomicBoolean live = new AtomicBoolean(false);//游戏面板是否存活

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
        GameMap.initTank(mode,level);
        addKeyListener(new KeyBoardListener());
        //executorService.submit(new MissileMove());
        executorService.submit(new Draw());
    }

    public static ExecutorService getExecutorService() {
        return executorService;
    }



    public static Mode getMode() {
        return mode;
    }


    public static AtomicBoolean getLive() {
        return live;
    }



    /**
     * 重绘函数
     */
    @Override
    synchronized public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        //绘制坦克
        for (Tank tank : GameMap.getTanks().values()) {
            tank.draw(g2);
        }
        //绘画墙体
        for (Wall wall : GameMap.getWalls().values()) {
            wall.draw(g2);
        }

        for(Surface surface: GameMap.getSurfaces().values()){
            surface.draw(g2);
        }

        //打印子弹
        for (Shell shell : GameMap.getShells().values()) {
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
        Graphics offscreen = OffScreenImage.getGraphics();    //设置一个内存画笔颜色为前景图片颜色
        Color c = offscreen.getColor();    //还是先保存前景颜色
        offscreen.setColor(Color.BLACK);    //设置内存画笔颜色为绿色
        offscreen.fillRect(0, 0, screenWidth, screenHeight);    //画成图片，大小为游戏大小
        offscreen.setColor(c);    //还原颜色
        g.drawImage(OffScreenImage, 0, 0, null);    //在界面画出保存的图片
        paint(offscreen);    //把内存画笔调用给paint
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

            //上下左右+小键盘0/空格为P1按键
//            if (key == KeyEvent.VK_N) {
//                nextLevel();//测试用，跳关卡
//            }
            if (key < 65) {
                if (GameMap.getTanks().get(P1_TAG) != null) {
                    PlayerTank p1 = (PlayerTank) GameMap.getTanks().get(P1_TAG);
                    p1.setKey(key);
                    p1.setMove(true);
                }
                if (key == KeyEvent.VK_ESCAPE) {
                    ShutDown();
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            super.keyReleased(e);
            int key = e.getKeyCode();
            if (key < 65&&key!=KeyEvent.VK_SPACE) {
                checkNextMove(key, GameMap.getTanks().get(P1_TAG), null, P1_TAG);
            } else if (key == KeyEvent.VK_NUMPAD0||key==KeyEvent.VK_SPACE) {
                PlayerTank p1 = (PlayerTank) GameMap.getTanks().get(P1_TAG);
                if (GameMap.getShells().get(P1_TAG) != null) {
                    System.out.println("当前仍有子弹在飞行！射击失败");
                    return;
                }
                p1.GetMoveDirection(KeyEvent.VK_NUMPAD0);//空格转化为NUMPAD0
            }
            }


        private void checkNextMove(int key, Tank tank, Tank o, int p1Tag) {
            if (tank != o) {
                if (key != KeyEvent.VK_NUMPAD0 && key == GameMap.getTanks().get(p1Tag).getKey()) {
                    PlayerTank p1 = (PlayerTank) GameMap.getTanks().get(p1Tag);
                    p1.setMove(false);//若true则连续移动
                } else {
                    PlayerTank p1 = (PlayerTank) GameMap.getTanks().get(p1Tag);
                    p1.GetMoveDirection(key);

                }
            }
        }
    }

    public static void ShutDown() {
        GamePanel.live.getAndSet(false);
        //停止所有的线程
        for (Tank tank : GameMap.getTanks().values()) {
            tank.setAlive(false);
        }
        executorService.shutdown();
        executorService = Executors.newCachedThreadPool();
        GameMap.getTanks().clear();
        GameMap.getWalls().clear();
        GameMap.getShells().clear();
        //gamePanel=null;
    }
    public static void reStart() {
        GamePanel.live.getAndSet(false);
        //停止所有的线程
        for (Tank tank : GameMap.getTanks().values()) {
            tank.setAlive(false);
        }
        executorService.shutdown();
        executorService = Executors.newCachedThreadPool();
        GameMap.getTanks().clear();
        GameMap.getWalls().clear();
        GameMap.getShells().clear();
        GamePanel backup=gamePanel;
        gamePanel=null;
        backup.thisLevel();

    }
    public static void win(){
        //ShutDown();
        GamePanel.live.getAndSet(false);
        GameMap.getWalls().clear();
        String p1Name= SettingsUtils.readGameSettings().getPlayerName();
        Player player=new Player(p1Name,LevelPanel.getPlayTime(), gamePanel.level);
        gamePanel.showResult(player,null);
        try {
            RankUtils.saveToPlayerList(player);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void goNextLevel(){
        System.out.println("Go Next Level");
        gamePanel.nextLevel();
    }
    private void showResult(Player p1,Player p2){
        ResultPanel resultPanel=new ResultPanel(p1,p2);
        setVisible(false);
        play.setLayout(null);
        play.setContentPane(resultPanel);
        play.setBounds(resultPanel.getBounds());
        play.setVisible(true);
        resultPanel.requestFocus();

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
        LevelPanel levelPanel = new LevelPanel(level, play, mode);
        play.setBounds(levelPanel.getBounds());
        play.setContentPane(levelPanel);
        play.setVisible(true);
        play.setResizable(false);
        levelPanel.requestFocus();
    }
}
