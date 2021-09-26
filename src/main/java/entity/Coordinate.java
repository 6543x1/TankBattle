package entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Coordinate {
    //二维地图坐标，用于寻路系统
    public int x;
    public int y;
    public Coordinate nextPoint;

    public Coordinate(int x,int y){
        this.x=x;
        this.y=y;
    }
}
