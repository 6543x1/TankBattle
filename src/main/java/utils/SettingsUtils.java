package utils;

import myEnum.Difficulty;

import java.io.*;
import java.util.Iterator;
import java.util.Properties;

public class SettingsUtils {
    private boolean music;
    private boolean soundEffect;
    private String playerName;
    private Difficulty difficulty;
    private static SettingsUtils settingsUtils;


    private SettingsUtils() {

    }

    public static SettingsUtils readGameSettings() {
        if (settingsUtils != null) {
            return settingsUtils;
        }
        settingsUtils = new SettingsUtils(true, true, "player1",Difficulty.easy);
        settingsUtils.readSettings();
        return settingsUtils;
    }

    public static void saveGameSettings(SettingsUtils s1) {
        s1.saveSettings();
    }

    public static void reverseSetMusic() {
        if (settingsUtils.isMusic()) {
            settingsUtils.setMusic(false);
        } else {
            settingsUtils.setMusic(true);
        }
        MusicUtils.play();
        saveGameSettings(settingsUtils);
    }
    public static void setPlayer1Name(String name) {
      settingsUtils.setPlayerName(name);
      settingsUtils.saveSettings();

    }
    public static void setGameDifficulty(Difficulty difficulty) {
        settingsUtils.setDifficulty(difficulty);
        settingsUtils.saveSettings();

    }

    private void saveSettings() {
        Properties properties = new Properties();
        properties.setProperty("music", String.valueOf(music));
        properties.setProperty("soundEffect", String.valueOf(false));
        properties.setProperty("playerName", playerName);
        properties.setProperty("difficulty", String.valueOf(difficulty));
        File file = null;
        file = new File(SettingsUtils.class.getResource("/settings.properties").getPath());
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            properties.store(fileOutputStream, "Game Settings");
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void readSettings() {
        Properties properties = new Properties();
        File file = null;
        file = new File(SettingsUtils.class.getResource("/settings.properties").getPath());
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            properties.load(fileInputStream);
            /**begin*******直接遍历文件key值获取*******begin*/
            Iterator<String> iterator = properties.stringPropertyNames().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                System.out.println(key + ":" + properties.getProperty(key));
            }
            /**end*******直接遍历文件key值获取*******end*/
            /**begin*******在知道Key值的情况下，直接getProperty即可获取*******begin*/
            music = Boolean.parseBoolean(properties.getProperty("music"));
            soundEffect = Boolean.parseBoolean(properties.getProperty("soundEffect"));
            playerName = properties.getProperty("playerName");
            difficulty=Difficulty.valueOf(properties.getProperty("difficulty"));
            /**end*******在知道Key值的情况下，直接getProperty即可获取*******end*/
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("游戏将使用默认设置!");
            readGameSettings();
            saveSettings();
        }


    }

    private SettingsUtils(boolean music, boolean soundEffect, String playerName,Difficulty difficulty) {
        this.music = music;
        this.soundEffect = soundEffect;
        this.playerName = playerName;
        this.difficulty=difficulty;
    }

    public boolean isMusic() {
        return music;
    }

    public void setMusic(boolean music) {
        this.music = music;
    }

    public boolean isSoundEffect() {
        return soundEffect;
    }

    public void setSoundEffect(boolean soundEffect) {
        this.soundEffect = soundEffect;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
}
