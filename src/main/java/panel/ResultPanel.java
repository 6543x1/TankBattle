package panel;

import gameElements.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ResultPanel extends JPanel implements KeyListener {
    private boolean alive;
    private final Player p1;
    private final Player p2;
    public ResultPanel(Player player1,Player player2){
        alive=true;
        p1=player1;
        p2=player2;
        setForeground(Color.GRAY);
        setBackground(Color.BLACK);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((d.width- 100)/2, (d.height- 600)/2, 720, 600);
        setPreferredSize(new Dimension(720,600));
        setSize(720,600);//Panel大小实际上会超出窗口大小......
        setLayout(null);
        GamePanel.getExecutorService().submit(()->{while(alive){
            repaint();
        }
        });
        addKeyListener(this);

    }

    @Override
    public void paint(Graphics g){
        Graphics2D g2=(Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("黑体", Font.BOLD, 20));
        g2.drawString(p1.toString(), 26, 30);
        g2.setColor(Color.RED);
        g2.drawString("按回车进入下一关", 80, 80);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_ENTER){
            alive=false;
            GamePanel.goNextLevel();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
