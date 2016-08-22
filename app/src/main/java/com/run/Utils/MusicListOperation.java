package com.run.Utils;

import android.content.Context;

import com.run.Bean.LocalMusic;
import com.run.Bean.PlayerStatus;
import com.run.coolmusic.CoolMusicApplication;

import java.util.List;
import java.util.Random;

/**
 * Created by Albert on 2015/9/4.
 */
public class MusicListOperation {

    private static int nextMusicPosition;

    /**
    * 在传入的参数列表当中选择下一首音乐,通过choice选项决定选择规则
    * 或许将来返回类型以及参数要有变动的，当有网络音乐播放功能的时候
    * 应当面向抽象编程
    *
    * @param context Activity上下文，用于获取播放器循环方式
    * @param currentPosition 当前播放音乐在列表中的位置信息，index
    * @param _list 音乐列表，从中选择一首音乐
    *
    * @return 返回获取到的音乐
    * */
    public static LocalMusic getNext(Context context, int currentPosition, List<LocalMusic> _list) {
        CoolMusicApplication application = (CoolMusicApplication)context.getApplicationContext();
        int choice = application.getPlayerStatus().getPlayStateInteger();
        switch(choice) {
            case PlayerStatus.LIST_CIRCLE:
                return getNextByListCircle(currentPosition, _list);
            case PlayerStatus.RANDOM:
                return getNextByRandom(_list);
            case PlayerStatus.SINGLE:
                return getNextBySingle(currentPosition, _list);
            default:
                return null;
        }
    }
    /**
     * 用于通过列表循环的方式获取需要播放的下一首音乐
     * */
    private static LocalMusic getNextByListCircle(int currentPosition, List<LocalMusic> _list) {
        int listLength = _list.size();
        int nextPosition = (currentPosition + 1) % listLength;
        nextMusicPosition = nextPosition;
        return _list.get(nextPosition);
    }

    /**
     * 用于通过随机选的方式获取需要播放的下一首音乐
     * */
    private static LocalMusic getNextByRandom(List<LocalMusic> _list) {
        Random random = new Random();
        int nextPosition = random.nextInt(_list.size());
        nextMusicPosition = nextPosition;
        return _list.get(nextPosition);
    }

    /**
     * 继续播放当前音乐
     * */
    private static LocalMusic getNextBySingle(int position, List<LocalMusic> _list){
        nextMusicPosition = position;
        return _list.get(position);
    }

    public static int getNextMusicPosition() {
        return nextMusicPosition;
    }
}
