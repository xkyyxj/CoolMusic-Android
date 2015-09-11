package com.run.coolmusic;

import android.app.Application;

import com.run.Bean.PlayerStatus;
import com.run.Services.LocalMusicGetter;

/**
 * Created by Albert on 2015/8/27.
 */
public class CoolMusicApplication extends Application {

    public static final String COOL_MUSIC_TAG = "CoolMusic:";

    private LocalMusicGetter localMusicList;

    private PlayerStatus playerStatus;

    @Override
    public void onCreate() {
        super.onCreate();
        playerStatus = new PlayerStatus();
    }

    public LocalMusicGetter getLocalMusicList() {
        return localMusicList;
    }

    public void setLocalMusicList(LocalMusicGetter localMusicList) {
        this.localMusicList = localMusicList;
    }

    public PlayerStatus getPlayerStatus() {
        return playerStatus;
    }
}
