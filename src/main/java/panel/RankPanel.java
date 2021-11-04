package panel;


import entity.Player;
import utils.RankUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Set;

public class RankPanel extends JPanel {
    private JFrame mainFrame;
    private JFrame play;
    private List<Player> playerList;
    public RankPanel(JFrame play,JFrame mainframe){
        this.play=play;
        this.mainFrame=mainframe;
        setBackground(Color.WHITE);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((d.width- 900)/2, (d.height- 600)/2, 600, 600);
        setSize(620,640);//Panel大小实际上会超出窗口大小......
        setLayout(null);
        JList<Player> jList=new JList<>();
        jList.setPreferredSize(new Dimension(200,100));
        jList.setSize(300,300);
        jList.setFont(new Font("宋体",Font.PLAIN,15));
        try {
            jList.setListData(RankUtils.getPlayerList().toArray(new Player[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }
        add(jList);
        setVisible(true);
        JButton backButton=new JButton("返回首页");
        backButton.setBounds(224, 472, 150, 33);
        backButton.addActionListener((e -> {
            backToMain();
        }));
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        add(backButton);
    }
    private void backToMain(){
        play.setVisible(false);
        mainFrame.setVisible(true);
    }
}
