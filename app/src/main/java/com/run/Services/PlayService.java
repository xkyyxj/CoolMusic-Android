package com.run.Services;

import android.media.MediaPlayer;

import com.run.Bean.LocalMusic;

import java.io.IOException;

/**
 * Created by Albert on 2015/8/27.
 */
public class PlayService {

    private boolean isPlaying = false;//是否正在播放
    private boolean hasPaused = false;//是否正在暂停播放

    private LocalMusic nextMusic;

    private OnPlayServiceCompletionListener listener;

    MediaPlayer player = null;

    public PlayService()
    {
        player = new MediaPlayer();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(listener != null)
                    listener.onCompletion(PlayService.this);
            }
        });
    }

    /**
    * 播放音乐
    *
    * @param filePathString 播放音乐文件地址
    * */
    public void play(String filePathString) throws IOException {
        if(player == null) {
            player = new MediaPlayer();
        }
        if(isPlaying || hasPaused)
        {
            //正在播放中的时候，需要将MediaPlayer重置，否则播放新音乐的时候回出现错误
            player.stop();
            player.reset();
        }
        //重新设置数据源
        player.setDataSource(filePathString);
        player.prepare();
        player.start();
        //设置播放状态
        isPlaying = true;
    }

    public void stop() {
        player.stop();
        isPlaying = false;
        hasPaused = false;
    }

    public void pause()
    {
        player.pause();
        //播放状态切换成暂停
        isPlaying = false;
        //是否暂停过设置成true
        hasPaused = true;
    }

    public void reStart(){
        if(player != null && hasPaused) {
            player.start();
            isPlaying = true;
            hasPaused = false;
//        else
            //player被释放之后的处置措施
            //重建，并且应当保存上一次播放的歌曲信息以及播放时长
        }
    }

    public boolean isPlaying()
    {
        return isPlaying;
    }

    public void setNextMusic(LocalMusic nextMusic) {
        this.nextMusic = nextMusic;
    }

    public void setOnPlayServiceCompletionListener(OnPlayServiceCompletionListener listener) {
        this.listener = listener;
    }

    public interface OnPlayServiceCompletionListener {
        void onCompletion(PlayService playService);
    }
}
