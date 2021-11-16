import myEnum.Mode;
import panel.GamePanel;
import panel.LevelPanel;
import panel.RankPanel;
import panel.SettingsPanel;
import utils.MusicUtils;
import utils.SettingsUtils;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class StartGame extends JFrame {
    private JFrame play;

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
            SettingsUtils.readGameSettings();
            MusicUtils.play();
        });
    }

    private StartGame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 600) / 3, (screenSize.height - 600) / 3, 700, 600);
        getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        panel.setForeground(Color.GRAY);
        panel.setBackground(Color.BLACK);//生效的啊？
        getContentPane().add(panel);//将当前panel加入JFrame
        panel.setLayout(null);

        System.out.println(StartGame.class.getResource("/").getPath());
        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setBackground(Color.BLACK);
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setIcon(new ImageIcon(StartGame.class.getResource("/img/logo.png")));
        lblNewLabel.setBounds(190, 10, 320, 200);
        panel.add(lblNewLabel);

        JButton btnNewButton = new JButton("");
        //btnNewButton.setBackground(Color.BLACK);
        btnNewButton.setBounds(254, 243, 150, 35);
        btnNewButton.setIcon(new ImageIcon(StartGame.class.getResource("/img/single.png")));
        btnNewButton.addActionListener(e -> play(Mode.Single));
        btnNewButton.setBorderPainted(false);
        btnNewButton.setContentAreaFilled(false);
        panel.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("");
        btnNewButton_1.setBounds(254, 298, 150, 35);
        btnNewButton_1.setIcon(new ImageIcon(StartGame.class.getResource("/img/double.png")));
        btnNewButton_1.setBorderPainted(false);
        btnNewButton_1.addActionListener(e -> play(Mode.Double));
        btnNewButton_1.setContentAreaFilled(false);
        panel.add(btnNewButton_1);

        JButton jButton=new JButton("");
        jButton.setBounds(254, 353, 150, 35);
        jButton.setIcon(new ImageIcon(StartGame.class.getResource("/img/settings.png")));
        jButton.setBorderPainted(false);
        jButton.addActionListener(e->settings());
        jButton.setContentAreaFilled(false);
        panel.add(jButton);

        JButton rankButton=new JButton("");
        rankButton.setBounds(254, 408, 150, 35);
        rankButton.setIcon(new ImageIcon(StartGame.class.getResource("/img/playerRank.png")));
        rankButton.setBorderPainted(false);
        rankButton.setContentAreaFilled(false);
        rankButton.setBorderPainted(false);
        rankButton.addActionListener(e->rank());
        panel.add(rankButton);

    }

    private void play(Mode mode) {
        GamePanel.getLive().getAndSet(true);
        play = new JFrame("Live" + ":" + 0+ "s");
        play.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(false);
        LevelPanel levelPanel=new LevelPanel(1,play,mode);
        play.setBounds(levelPanel.getBounds());
        play.setContentPane(levelPanel);
        play.setVisible(true);
        play.setResizable(false);
        levelPanel.requestFocus();
    }
    private void settings(){
        play = new JFrame("游戏设置");
        play.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(false);
        JPanel jPanel=new SettingsPanel(play,this);
        play.setBounds(jPanel.getBounds());
        play.setContentPane(jPanel);
        play.setVisible(true);
        play.setResizable(false);
        jPanel.requestFocus();
    }

    private void rank(){
        play=new JFrame("排行榜");
        play.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(false);
        JPanel jPanel=new RankPanel(play,this);
        play.setBounds(jPanel.getBounds());
        play.setContentPane(jPanel);
        play.setVisible(true);
        play.setResizable(false);
        jPanel.requestFocus();

    }


}

