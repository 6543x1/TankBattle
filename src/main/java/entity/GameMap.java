package entity;

import myEnum.ObjType;

import java.util.concurrent.ConcurrentHashMap;

public class GameMap {
    //地图，储存除了子弹以外的东西
    //注意当使用Coord的x和y的时候是map[y][x]
    public volatile static ObjType[][] map;
    public volatile static ConcurrentHashMap<Integer, Tank> tanks = new ConcurrentHashMap<>();
    public volatile static ConcurrentHashMap<Integer,Shell> shells=new ConcurrentHashMap<>();
    public volatile static ConcurrentHashMap<Integer,Wall> walls=new ConcurrentHashMap<>();
}
