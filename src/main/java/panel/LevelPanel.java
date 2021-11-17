package panel;


import myEnum.Mode;
import utils.ImageUtils;

import javax.swing.*;
import java.awt.*;

public class LevelPanel extends JPanel {
    private int level;
    private final Mode mode;
    private final JFrame mainFrame;
    private GamePanel gamePanel;
    private JFrame play;
    private static int playTime;

    public static int getPlayTime() {
        return playTime;
    }

    public LevelPanel(int level, JFrame frame, Mode type) {

        this.mainFrame = frame;//这个是从start祖传来的JFrame
        this.level = level;
        this.mode = type;
        setForeground(Color.BLACK);
        setBackground(Color.WHITE);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((d.width- 900)/2, (d.height- 600)/2, 740, 640);
        setSize(740,640);//Panel大小实际上会超出窗口大小......
        setLayout(null);
        Thread t=new Thread(new LevelPanelThread());
        t.start();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2=(Graphics2D)g;
        g2.setColor(Color.GRAY);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setFont(new Font("黑体", Font.BOLD, 50));
        g2.setColor(Color.BLACK);
//        g2.drawString(levelStr, 260, 300);
        g2.drawImage(ImageUtils.getStage(),200,270,238,34,null);
        g2.drawString(String.valueOf(level),458,304);
        g2.setColor(Color.RED);
//        g2.drawString(ready, 270, 400);
//        System.out.println("Paint!");

    }

    private void gotoGamePanel() {
        GamePanel.getLive().getAndSet(true);
//        play = new JFrame("Live" + ":" + playTime++ + "s");
        play=mainFrame;
        play.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //setVisible(false);
        //play是新的坦克大战窗口;但是那个setVisible的LevelPanel在主函数的Frame里，而这个主函数的Frame没有隐藏
        gamePanel = GamePanel.newGamePanel(mode,level,play);
        play.setSize(900,900);
        //play.setSize(900,900);
        play.setLayout(null);
//        play.setLayout(new BorderLayout());
//        play.add(gamePanel,BorderLayout.CENTER);
//        play.add(new ScorePanel(),BorderLayout.EAST);
        play.setVisible(true);
        //play.add(gamePanel,BorderLayout.NORTH);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        play.setBounds((screenSize.width-600)/3,(screenSize.height-600)/3,740,640);
        //调整此处width
        play.setContentPane(gamePanel);
//        play.getContentPane().removeAll();
//        play.getContentPane().add(gamePanel);
//        play.getContentPane().validate();
//        play.setBounds(gamePanel.getBounds());
        play.setVisible(true);
        play.setResizable(false);
        gamePanel.requestFocus();
        new Thread(new CheckLive()).start();
    }
    class CheckLive implements Runnable {
        @Override
        public void run() {
            while (GamePanel.getLive().get()) {
                play.setTitle("TankBattle" + " Live" + playTime++ + "s");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            gamePanel.removeAll();
            play.remove(gamePanel);
            gamePanel = null;
            setVisible(true);
        }
    }

    private class LevelPanelThread implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 4; i++) {
                repaint();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            gotoGamePanel();
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
