package entity;

import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private int times;
    private int level;

    public Player(String name, int times, int level) {
        this.name = name;
        this.times = times;
        this.level = level;
    }

    @Override
    public String toString() {
        return
                "玩家姓名: " + name  +
                ", 通过关卡 " + level +
                ", 耗时 " + times+" 秒";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
