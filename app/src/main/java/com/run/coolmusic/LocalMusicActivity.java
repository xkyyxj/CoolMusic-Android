package com.run.coolmusic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.run.Adapter.LocalMusicListAdapter;
import com.run.Bean.LocalMusic;
import com.run.Bean.PlayerStatus;
import com.run.Services.LocalMusicGetter;
import com.run.Services.PlayService;
import com.run.Utils.MusicListOperation;

import java.io.IOException;
import java.util.List;

public class LocalMusicActivity extends Activity {

    public static final String LOCAL_MUSIC_ACTIVITY_TAG = "LocalMusicActivity:";

    private PlayService playService = null;
    private List<LocalMusic> localMusicList = null;

    private ListView musicList;
    private TextView title, musicName, musicArtist;
    private Button returnButton, configButton;
    private ImageView musicIcon;
    private ImageButton playing, next;
    private SeekBar progress;

    private LocalMusic nextMusic;

    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_local_music_list);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    * 查找加载View组件
    * 由Android Studio插件完成
    * */
    private void assignViews() {
        musicList = (ListView) findViewById(R.id.music_list);
        title = (TextView) findViewById(R.id.header_title);
        returnButton = (Button) findViewById(R.id.header_return_button);
        playing = (ImageButton) findViewById(R.id.playing);
        next = (ImageButton) findViewById(R.id.next_music);
        musicName = (TextView) findViewById(R.id.music_name);
        progress = (SeekBar) findViewById(R.id.playing_progress);
        musicIcon = (ImageView) findViewById(R.id.music_icon);
        musicArtist = (TextView) findViewById(R.id.music_artist);
        configButton = (Button) findViewById(R.id.config_button);
    }

    /*
    * 初始化工作
    * 初始化播放器服务PlayService
    * 初始化本地音乐列表localMusicList
    * 待完成工作：加载本地音乐时需要一个加载提示
    * */
    private void init()
    {
        //初始化音乐播放服务
        playService = new PlayService();
        playService.setOnPlayServiceCompletionListener(new LocalPlayServiceCompletionListener());
        //Application保存了一份本地音乐列表，从中取出
        CoolMusicApplication application = (CoolMusicApplication)getApplication();
        LocalMusicGetter localMusicGetter = application.getLocalMusicList();
        //若Application没有保存本地音乐列表，则向Application中注入一份，全局保存
        if(localMusicGetter == null) {
            localMusicGetter = new LocalMusicGetter(this);
            application.setLocalMusicList(localMusicGetter);
        }
        localMusicList = localMusicGetter.getMusicInfo();
        Log.e(CoolMusicApplication.COOL_MUSIC_TAG, "" + localMusicList.size());
        initView();
    }

    //初始化视图组件
    private void initView()
    {
        assignViews();
        //PlayBar 播放按钮设置背景图片
        playing.setImageResource(R.mipmap.play_button);
        //设置标题栏
        title.setText(R.string.local_music_title);
        //为各个组件设置监听
        //滑动条监听
        progress.setOnSeekBarChangeListener(new MySeekBarListener());
        //列表监听
        musicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickToPlay(position);
            }
        });
        //各个按钮监听
        LocalMusicButtonListener buttonListener = new LocalMusicButtonListener();
        returnButton.setOnClickListener(buttonListener);
        playing.setOnClickListener(buttonListener);
        next.setOnClickListener(buttonListener);
        configButton.setOnClickListener(buttonListener);
        //为代表本地音乐列表的ListView设置Adapter
        musicList.setAdapter(new LocalMusicListAdapter(this, localMusicList));
    }

    //用户点击列表某一项然后播放对应音乐
    private void clickToPlay(int position)
    {
        currentPosition = position;
        //获取对应音乐Bean
        LocalMusic temp = localMusicList.get(position);
        //设置playBar的相关UI信息
        musicName.setText(temp.getMusicNameString());
        musicArtist.setText(temp.getArtistString());
        //TODO 设置音乐的图片信息，也就是musicIcon
        //试图播放音乐
        try {
            playService.play(temp.getMusicPathString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //PlayBar 播放按钮背景图片切换 R.mipmap.play_button -> R.mipmap.pause
        playing.setImageResource(R.mipmap.pause);
        //设定播放器下一首要播放的音乐
        nextMusic = MusicListOperation.getNext(this,currentPosition,localMusicList);
    }

    private void completeAndPlay(PlayService playService) {
        try {
            playService.play(nextMusic.getMusicPathString());
            currentPosition = MusicListOperation.getNextMusicPosition();
        } catch (IOException e) {
            e.printStackTrace();
            //TODO 异常处理工作
        }
        //设置playBar的相关UI信息
        musicName.setText(nextMusic.getMusicNameString());
        musicArtist.setText(nextMusic.getArtistString());
        nextMusic = MusicListOperation.getNext(this,currentPosition,localMusicList);
    }

    private void setCircleWay() {
        //弹出对话框，使用户选择音乐循环方式
        AlertDialog.Builder builder = new AlertDialog.Builder(LocalMusicActivity.this);
        builder.setSingleChoiceItems(R.array.music_cricle_way, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //播放器状态对象存放在全局Application当中
                PlayerStatus playerStatus = ((CoolMusicApplication)getApplication()).getPlayerStatus();
                switch(which){
                    case 0:
                        playerStatus.setPlayStateInteger(PlayerStatus.SINGLE);
                        break;
                    case 1:
                        playerStatus.setPlayStateInteger(PlayerStatus.LIST_CIRCLE);
                        break;
                    case 2:
                        playerStatus.setPlayStateInteger(PlayerStatus.RANDOM);
                        break;
                }
                //每次切换播放模式的时候，都要重新获取下一首音乐信息，使被切换到的模式可以立即生效
                //避免上一次的模式生成的下一首音乐造成影响
                nextMusic = MusicListOperation.getNext(LocalMusicActivity.this,currentPosition,localMusicList);
            }
        }).show();
    }

    //音乐播放滑动条监听器
    class MySeekBarListener implements SeekBar.OnSeekBarChangeListener
    {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    //LocalMusicActivity按钮监听器（公用）
    class LocalMusicButtonListener implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch(id)
            {
                //标题栏返回按钮
                case R.id.header_return_button:
                    finish();
                    break;
                //播放栏播放\暂停按钮
                case R.id.playing:
                    boolean tempState = playService.isPlaying();
                    if(tempState) {
                        playService.pause();
                        playing.setImageResource(R.mipmap.play_button);
                    }
                    else {
                        playService.reStart();
                        playing.setImageResource(R.mipmap.pause);
                    }
                    break;
                //播放栏下一首音乐按钮
                case R.id.next_music:
                    completeAndPlay(playService);
                    break;
                case R.id.config_button:
                    setCircleWay();
                    break;
            }
        }
    }

    class LocalPlayServiceCompletionListener implements PlayService.OnPlayServiceCompletionListener {

        @Override
        public void onCompletion(PlayService playService) {
            completeAndPlay(playService);
        }
    }

}
