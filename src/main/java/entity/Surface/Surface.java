package entity.Surface;

import entity.VisualObj;

import java.awt.*;

public abstract class Surface extends VisualObj {
    public Surface(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public boolean isCollided(VisualObj visualObj) {
        return false;//所有的表面（草地、水面等）不会与其他物体发生碰撞，可以共存
    }
}
