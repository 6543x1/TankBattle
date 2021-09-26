package entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.awt.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public abstract class Wall extends VisualObj{
    protected int id;
    protected int hp;
    public Wall(int hp,int id,int x,int y,int width,int height){
        super(x,y,width,height);
        this.hp=hp;
        this.id=id;
    }
}
