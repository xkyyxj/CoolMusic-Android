package com.run.Bean;

/**
 * Created by Albert on 2015/9/4.
 */
public class PlayerStatus {

    //音乐循环播放方式
    public static final int SINGLE = 0;//单曲循环
    public static final int LIST_CIRCLE = 1;//列表循环
    public static final int RANDOM = 2;//随机播放

    private Integer playStateInteger = LIST_CIRCLE;

    public Integer getPlayStateInteger() {
        return playStateInteger;
    }

    public void setPlayStateInteger(Integer playStateInteger) {
        this.playStateInteger = playStateInteger;
    }
}
