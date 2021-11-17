package panel;

import myEnum.Difficulty;
import myEnum.Mode;
import utils.SettingsUtils;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {
    private final JFrame mainFrame;
    private final JFrame play;
    public SettingsPanel(JFrame frame,JFrame mainFrame) {

        this.mainFrame = mainFrame;//这个是从start祖传来的JFrame（窗口），以后所有的Panel均显示于此;
        this.play=frame;
        setForeground(Color.WHITE);
        setBackground(Color.BLACK);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((d.width- 900)/2, (d.height- 600)/2, 600, 600);
        setSize(720,640);//Panel大小实际上会超出窗口大小......
        setLayout(null);
        JTextField difficulty=new JTextField();
        difficulty.setBorder(null);
        difficulty.setBackground(Color.BLACK);
        difficulty.setForeground(Color.YELLOW);
        difficulty.setText("当前难度:"+SettingsUtils.readGameSettings().getDifficulty()+",重启生效");
        difficulty.setBounds(244,287,150,33);
        add(difficulty);
        Font font=new Font("黑体",Font.PLAIN,15);
        JButton easyButton=new JButton("简单");
        easyButton.setMargin(new Insets(0,0,0,0));
        easyButton.addActionListener((e)->{
            SettingsUtils.setGameDifficulty(Difficulty.easy);
        });
        easyButton.setBounds(244, 320, 50, 33);
        add(easyButton);
        JButton normalButton=new JButton("普通");
        normalButton.setMargin(new Insets(0,0,0,0));
        normalButton.addActionListener((e)->{
            SettingsUtils.setGameDifficulty(Difficulty.normal);
        });
        normalButton.setBounds(294, 320, 50, 33);
        add(normalButton);
        JButton hardButton=new JButton("困难");
        hardButton.addActionListener((e)->{
            SettingsUtils.setGameDifficulty(Difficulty.hard);
        });
        hardButton.setBounds(344, 320, 50, 33);
        hardButton.setMargin(new Insets(0,0,0,0));
        add(hardButton);
        JButton musicButton=new JButton("音乐开关");
        musicButton.addActionListener((e)->{
            SettingsUtils.reverseSetMusic();
        });
        musicButton.setBounds(244, 353, 150, 33);
        add(musicButton);
        JButton soundEffectButton=new JButton("音效开关");
        soundEffectButton.setBounds(244, 386, 150, 33);
        add(soundEffectButton);
        final JTextField textField = new JTextField(SettingsUtils.readGameSettings().getPlayerName(),10);
        if(SettingsUtils.readGameSettings().getPlayerName().equals("")){
            textField.setText("未设置玩家名");
        }
        textField.setForeground(Color.WHITE);
        textField.setLocation(244,419);
        textField.setSize(150,20);
        textField.setFont(new Font(null, Font.PLAIN, 18));
        font=new Font("黑体",Font.PLAIN,20);
        textField.setForeground(Color.WHITE);
        textField.setBackground(Color.GRAY);
        add(textField);
        JButton setNameButton=new JButton("更改姓名");
        setNameButton.setBounds(244,439,150,33);
        setNameButton.addActionListener(e -> {
            SettingsUtils.setPlayer1Name(textField.getText());
        });
        add(setNameButton);
        JButton backButton=new JButton("返回首页");
        backButton.setForeground(Color.WHITE);
        backButton.setBounds(244, 472, 150, 33);
        backButton.addActionListener((e -> {
            backToMain();
        }));
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);

        add(backButton);
//        Thread t = new LevelPanelThread();
//        t.start();

        //executorService.submit(new LevelPanelThread());
    }
    private void backToMain(){
        play.setVisible(false);
        mainFrame.setVisible(true);
    }
}
