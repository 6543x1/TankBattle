package panel;

import myEnum.Mode;
import utils.SettingsUtils;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {
    private JFrame mainFrame;
    private JFrame play;
    public SettingsPanel(JFrame frame,JFrame mainFrame) {

        this.mainFrame = mainFrame;//这个是从start祖传来的JFrame（窗口），以后所有的Panel均显示于此;
        this.play=frame;
        setForeground(Color.BLACK);
        setBackground(Color.WHITE);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((d.width- 900)/2, (d.height- 600)/2, 900, 600);
        setSize(920,640);//Panel大小实际上会超出窗口大小......
        setLayout(null);
        JButton musicButton=new JButton("音乐开关");
        musicButton.addActionListener((e)->{
            SettingsUtils.reverseSetMusic();
        });
        musicButton.setBounds(224, 353, 150, 33);
        add(musicButton);
        JButton soundEffectButton=new JButton("音效开关");
        soundEffectButton.setBounds(224, 386, 150, 33);
        add(soundEffectButton);
        final JTextField textField = new JTextField(SettingsUtils.readGameSettings().getPlayerName(),10);
        textField.setLocation(224,419);
        textField.setSize(150,20);
        textField.setFont(new Font(null, Font.PLAIN, 20));
        textField.setForeground(Color.BLACK);
        textField.setBackground(Color.WHITE);
        add(textField);
        JButton setNameButton=new JButton("提交姓名");
        setNameButton.setBounds(224,439,150,33);
        setNameButton.addActionListener(e -> {
            SettingsUtils.setPlayer1Name(textField.getText());
        });
        add(setNameButton);
        JButton backButton=new JButton("返回首页");
        backButton.setBounds(224, 472, 150, 33);
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
