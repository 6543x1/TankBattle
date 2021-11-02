package panel;

import myEnum.Mode;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {
    private JFrame mainFrame;
    public SettingsPanel(JFrame frame) {

        this.mainFrame = frame;//这个是从start祖传来的JFrame（窗口），以后所有的Panel均显示于此;
        setForeground(Color.BLACK);
        setBackground(Color.WHITE);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((d.width- 900)/2, (d.height- 600)/2, 900, 600);
        setSize(920,640);//Panel大小实际上会超出窗口大小......
        setLayout(null);
        JButton jButton=new JButton("测试开关");
        jButton.setBounds(224, 353, 150, 33);
        add(jButton);
//        Thread t = new LevelPanelThread();
//        t.start();

        //executorService.submit(new LevelPanelThread());
    }
}
