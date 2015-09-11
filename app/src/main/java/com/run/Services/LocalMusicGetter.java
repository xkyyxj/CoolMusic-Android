package com.run.Services;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.run.Bean.LocalMusic;
import com.run.coolmusic.CoolMusicApplication;

import java.io.File;
import java.util.ArrayList;

/**
 * Author:王庆春
 * Time:2015.8.27 16:56
 * 用于获取本机上的本地音乐
 */

//problem:music_art_path 没有用
public class LocalMusicGetter {

    public static final String LOCAL_MUSIC_GETTER_TAG = "LocalMusicGetter:";

    public static final int MUSIC_FILE_PATH = 1;
    public static final int MUSIC_NAME = 2;
    public static final int MUSIC_ARTIST = 4;
    public static final int MUSIC_ART_PATH = 8;

    int music_column_index = 0;
    String music_album_key = null;

    Context context = null;
    ContentResolver music_resolver = null, album_resolver = null;
    Cursor music_cursor = null, album_cursor = null;
    private ArrayList<LocalMusic> music_list = null;

    private static LocalMusicGetter music_info = null;

    public LocalMusicGetter(Context context) {
        this.context = context;
        initialize();
    }

    public void initialize() {
        String music_file_path,music_name,music_artist,music_art_path;
        music_list = new ArrayList<LocalMusic>();
        music_resolver = this.context.getContentResolver();
        music_cursor = music_resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,null);
        if (null != music_cursor && music_cursor.getCount() > 0) {
            String artist = null;
            for (music_cursor.moveToFirst(); !music_cursor.isAfterLast(); music_cursor.moveToNext()) {
                music_column_index = music_cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
                music_file_path = music_cursor.getString(music_column_index);
                music_column_index = music_cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE);
                music_name = music_cursor.getString(music_column_index);
                music_column_index = music_cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST);
                artist = music_cursor.getString(music_column_index);
                if (artist != null && !artist.equals(""))
                    music_artist = music_cursor.getString(music_column_index);
                else
                    music_artist = "";
                music_column_index = music_cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_KEY);

                music_album_key = music_cursor.getString(music_column_index);
                String[] arg_arr = { music_album_key };
                album_resolver = this.context.getContentResolver();
                album_cursor = album_resolver.query(
                        MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, null,
                        MediaStore.Audio.AudioColumns.ALBUM_KEY + " = ?",
                        arg_arr, null);
                if (null != album_cursor && album_cursor.getCount() > 0) {
                    album_cursor.moveToFirst();
                    int album_art_index = album_cursor.getColumnIndex(MediaStore.Audio.AlbumColumns.ALBUM_ART);
                    music_album_key = album_cursor.getString(album_art_index);
                    if (music_album_key != null && !music_album_key.equals(""))
                        music_art_path = music_album_key;
                }
                music_art_path = "null";
                album_cursor.close();
                music_list.add(createMusicBean(music_name,music_file_path,music_artist));
                Log.i(CoolMusicApplication.COOL_MUSIC_TAG,"running?");
            }
        }
        music_cursor.close();
        Log.v("music_infor_list", "" + getMusicNumber());
    }

    private LocalMusic createMusicBean(String music_name,String music_path,String music_artist)
    {
        LocalMusic bean = new LocalMusic();
        bean.setArtistString(music_artist);
        bean.setMusicNameString(music_name);
        bean.setMusicPathString(music_path);
        return bean;
    }

    public ArrayList<LocalMusic> getMusicInfo() {
        return music_list;
    }

    public int getMusicNumber()
    {
        return music_list == null ? 0 : music_list.size();
    }

    public void deleteMusic(int position)
    {
        File file = new File(music_list.get(position).getMusicPathString());
        if(file != null && file.exists())
            file.delete();
    }

}
