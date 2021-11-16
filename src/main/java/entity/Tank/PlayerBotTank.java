package entity.Tank;

import entity.Map.GameMap;
import myEnum.Direction;
import panel.GamePanel;
import utils.ImageUtils;

import java.awt.event.KeyEvent;
import java.util.Random;

public class PlayerBotTank extends PlayerTank {
    private int count;
    public PlayerBotTank(int x, int y, Direction direction, int id) {
        super(x, y, direction, id);
        count=0;
        GamePanel.getExecutorService().submit(new RandomMoveThread());
        GamePanel.getExecutorService().submit(new MyTankMove());//好像p1按了0就会动
        setMove(true);//居然是因为这句话动不了 我傻了
        super.HP=40;
        super.fullHp=40;
    }


    class RandomMoveThread implements Runnable {
        @Override
        public void run() {
            while (alive) {
                key = RandomMove();
                System.out.println("p2 go: " + curDirection);
//                GetMoveDirection(key);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + ":休眠中断");
                }
            }
        }

    }
    private int RandomMove() {
        if(++count<20){
            int n;
            switch (curDirection) {
                case DOWN:
                    n = KeyEvent.VK_DOWN;
                    break;
                case UP:
                    n = KeyEvent.VK_UP;
                    break;
                case RIGHT:
                    n = KeyEvent.VK_RIGHT;
                    break;
                case LEFT:
                    n = KeyEvent.VK_LEFT;
                    break;
                default:
                    n = KeyEvent.VK_NUMPAD0;
            };
            return n;
        }
        int[] ops = {KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_NUMPAD0};
        Random random = new Random(System.currentTimeMillis());
        if(key==KeyEvent.VK_NUMPAD0&&willShootBase()){
            return ops[random.nextInt(4)];//避免向基地射击
        }
        return ops[random.nextInt(5)];
    }
    private boolean willShootBase(){
        if(curDirection==Direction.DOWN&& GameMap.getBaseX()==getX()){
            return true;
        }
        else if(curDirection==Direction.LEFT&&GameMap.getBaseY()==getY()&&GameMap.getBaseX()-getX()<=0){
            return true;
        }
        else if(curDirection==Direction.RIGHT&&GameMap.getBaseY()==getY()&&GameMap.getBaseX()-getX()>=0){
            return true;
        }
        return false;
    }
    @Override
    protected void changeDirectionImage(){
        switch (curDirection){
            case UP:
                setImage(ImageUtils.getP2upImage());
                break;
            case DOWN:
                setImage(ImageUtils.getP2downImage());
                break;
            case LEFT:
                setImage(ImageUtils.getP2leftImage());
                break;
            case RIGHT:
                setImage(ImageUtils.getP2rightImage());
                break;
        }
    }
    @Override
    protected String getName() {
        StringBuilder val = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            // 输出字母还是数字
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 字符串
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 取得大写字母还是小写字母
                //int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                int choice = 65;//默认用大写了，六位重复率应该比较低吧
                val.append((char) (choice + random.nextInt(26)));
            } else { // 数字
                val.append(String.valueOf(random.nextInt(10)));
            }
        }
        return val.toString();
    }

}
