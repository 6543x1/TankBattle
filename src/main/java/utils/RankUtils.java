package utils;

import entity.Player;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class RankUtils {
    private List<Player> list;
    private static RankUtils rankUtils;

    public static List<Player> getPlayerList() throws Exception{
        if(rankUtils==null){
            rankUtils=new RankUtils();
        }
        FileInputStream fileInputStream = new FileInputStream("playerList.dat");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        try {
            rankUtils.list= (List<Player>) objectInputStream.readObject();
            System.out.println(rankUtils.list);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return rankUtils.list;
    }
    public static void saveToPlayerList(Player player) throws Exception{
        List<Player> playerList=getPlayerList();
        playerList.add(player);
        FileOutputStream fileOutputStream = new FileOutputStream("playerList.dat");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(playerList);
        objectOutputStream.flush();
        objectOutputStream.close();
    }

    private RankUtils(){}


}
