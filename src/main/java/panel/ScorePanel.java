package panel;

import javax.swing.*;
import java.awt.*;

public class ScorePanel extends JPanel {
    public ScorePanel(){
        setForeground(Color.GRAY);
        setBackground(Color.WHITE);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        //setBounds((d.width- 100)/2, (d.height- 600)/2, 100, 600);
        setPreferredSize(new Dimension(100,600));
        //setSize(120,640);//Panel大小实际上会超出窗口大小......
        setLayout(new BorderLayout());
        GamePanel.executorService.submit(()->{while(GamePanel.live.get()){
                repaint();
            }
        });
    }
    @Override
    public void paint(Graphics g) {
        Graphics2D g2=(Graphics2D)g;
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setFont(new Font("黑体", Font.BOLD, 20));
        g2.setColor(Color.GREEN);
        g2.drawString("Hello", 26, 30);
        g2.setColor(Color.RED);
        g2.drawString("World", 80, 80);
        //System.out.println("Paint!");
    }
}
