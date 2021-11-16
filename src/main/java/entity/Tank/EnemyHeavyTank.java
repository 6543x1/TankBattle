package entity.Tank;

import myEnum.Direction;
import utils.ImageUtils;

public class EnemyHeavyTank extends EnemyTank{

    public EnemyHeavyTank(int x, int y, Direction direction, int id) {
        super(x, y, direction, id);
        setHP(30);
        setSpeed(15);
    }
    //子类重写父类方法，调用时会调用子类重写之后的方法!
    //只有一种方法会调用父类方法:子类中super.调用,否则调用子类的方法，一定是子类重写过的
    @Override
    protected void changeDirectionImage(){
        switch (curDirection){
            case UP:
                setImage(ImageUtils.getHeavyUpImage());
                break;
            case DOWN:
                setImage(ImageUtils.getHeavyDownImage());
                break;
            case LEFT:
                setImage(ImageUtils.getHeavyLeftImage());
                break;
            case RIGHT:
                setImage(ImageUtils.getHeavyRightImage());
                break;
        }
    }
}
