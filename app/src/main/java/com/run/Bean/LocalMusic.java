package com.run.Bean;

/**
 * Created by Albert on 2015/8/27.
 */
public class LocalMusic {

    private String musicNameString; //音乐名称
    private String artistString; //音乐艺术家
    private String musicPathString; //文件路径
    private Integer musicTimeLengthInt; //秒级计数

    public String getArtistString() {
        return artistString;
    }

    public void setArtistString(String artistString) {
        this.artistString = artistString;
    }

    public String getMusicNameString() {
        return musicNameString;
    }

    public void setMusicNameString(String musicNameString) {
        this.musicNameString = musicNameString;
    }

    public String getMusicPathString() {
        return musicPathString;
    }

    public void setMusicPathString(String musicPathString) {
        this.musicPathString = musicPathString;
    }

    public Integer getMusicTimeLengthInt() {
        return musicTimeLengthInt;
    }

    public void setMusicTimeLengthInt(Integer musicTimeLengthInt) {
        this.musicTimeLengthInt = musicTimeLengthInt;
    }
}
