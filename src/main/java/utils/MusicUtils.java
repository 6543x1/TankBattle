package utils;

import javax.sound.sampled.*;
import java.io.File;

public class MusicUtils {
    private Clip clip;
    private static MusicUtils musicUtils;

    public static void play(){
        if(musicUtils==null){
            musicUtils=new MusicUtils();
        }
        if(!SettingsUtils.readGameSettings().isMusic()){
            if(musicUtils!=null&&musicUtils.clip!=null){
                musicUtils.closeBGMusic();
            }
            return;
        }
        try {
            musicUtils.playBGMusic();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MusicUtils(){

    }
    private void playBGMusic() throws Exception{
        File file = new File(MusicUtils.class.getResource("/sound/start.wav").getPath());
        AudioInputStream am;
        am = AudioSystem.getAudioInputStream(file);
        clip = AudioSystem.getClip();
        clip.open(am);
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);//不设置这句话就只放一遍
        //只要程序一直运行，这个就会不停响下去
    }
    private void closeBGMusic(){
        clip.close();
    }
}
