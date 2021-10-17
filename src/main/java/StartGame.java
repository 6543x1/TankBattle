import myEnum.Mode;
import panel.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StartGame extends JFrame {
    public static ExecutorService executorService = Executors.newCachedThreadPool();
    private JFrame play;
    private GamePanel gamePanel = null;
    private static int PlayTime = 0;

    public static void main(String[] args) {
        //使用EventQueue来使方法结束时资源被释放
        EventQueue.invokeLater(() -> {
            try {
                StartGame frame = new StartGame();
                frame.setResizable(false);
                frame.setVisible(true);
                frame.setTitle("坦克大战_Jessie Lin Design");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private StartGame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 600) / 3, (screenSize.height - 600) / 3, 600, 600);
        getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        panel.setForeground(Color.GRAY);
        panel.setBackground(Color.WHITE);
        getContentPane().add(panel);
        panel.setLayout(null);

        System.out.println(StartGame.class.getResource("/").getPath());
        JLabel lblNewLabel = new JLabel("New label");
        lblNewLabel.setBackground(Color.WHITE);
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setIcon(new ImageIcon(StartGame.class.getResource("/img/tankBattle2.png")));
        lblNewLabel.setBounds(10, 10, 577, 213);
        panel.add(lblNewLabel);

        JButton btnNewButton = new JButton("");
        btnNewButton.setBackground(Color.BLACK);
        btnNewButton.setBounds(224, 243, 144, 34);
        btnNewButton.setIcon(new ImageIcon(StartGame.class.getResource("/img/SinglePlayer.gif")));
        btnNewButton.addActionListener(e -> play(Mode.Single));
        btnNewButton.setBorderPainted(false);
        panel.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("");
        btnNewButton_1.setBounds(224, 298, 144, 34);
        btnNewButton_1.setIcon(new ImageIcon(StartGame.class.getResource("/img/DoublePlayer.gif")));
        btnNewButton_1.setBorderPainted(false);
        //btnNewButton_1.addActionListener(e -> play(Mode.Double));
        panel.add(btnNewButton_1);

    }

    private void play(Mode mode) {
        GamePanel.live.getAndSet(true);
        play = new JFrame("Live" + ":" + PlayTime++ + "s");
        play.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(false);
        gamePanel = new GamePanel(mode);
        play.setContentPane(gamePanel);
        play.setBounds(gamePanel.getBounds());
        play.setVisible(true);
        play.setResizable(false);
        gamePanel.requestFocus();
        new Thread(new CheckLive()).start();
    }


    class CheckLive implements Runnable {
        @Override
        public void run() {
            while (GamePanel.live.get()) {
                play.setTitle("TankBattle" + " Live" + PlayTime++ + "s");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            gamePanel.removeAll();
            play.remove(gamePanel);
            play.dispose();
            play = null;
            gamePanel = null;
            setVisible(true);
        }
    }

}

