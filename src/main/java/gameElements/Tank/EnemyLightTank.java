package gameElements.Tank;

import myEnum.Direction;
import utils.ImageUtils;

public class EnemyLightTank extends EnemyTank{
    public EnemyLightTank(int x, int y, Direction direction, int id) {
        super(x, y, direction, id);
        setHP(10);
        setSpeed(25);
    }
    @Override
    protected void changeDirectionImage(){
        switch (curDirection){
            case UP:
                setImage(ImageUtils.getLightUpImage());
                break;
            case DOWN:
                setImage(ImageUtils.getLightDownImage());
                break;
            case LEFT:
                setImage(ImageUtils.getLightLeftImage());
                break;
            case RIGHT:
                setImage(ImageUtils.getLightRightImage());
                break;
        }
    }
}
