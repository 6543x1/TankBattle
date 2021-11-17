package panel;


import gameElements.Player;
import utils.RankUtils;

import javax.swing.*;
import java.awt.*;

public class RankPanel extends JPanel {
    private final JFrame mainFrame;
    private final JFrame play;
    public RankPanel(JFrame play,JFrame mainframe){
        this.play=play;
        this.mainFrame=mainframe;
        setBackground(Color.BLACK);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((d.width- 900)/2, (d.height- 600)/2, 600, 600);
        setSize(620,640);//Panel大小实际上会超出窗口大小......
        setLayout(null);
//        String[] columnNames={"排名","玩家名","通关数","耗时"};
//        JTable jTable=new JTable(null,columnNames);
        JList<Player> jList=new JList<>();
        jList.setPreferredSize(new Dimension(600,300));
        jList.setSize(600,300);
        jList.setFont(new Font("宋体",Font.PLAIN,15));
        jList.setBackground(Color.BLACK);
        jList.setForeground(Color.YELLOW);
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
