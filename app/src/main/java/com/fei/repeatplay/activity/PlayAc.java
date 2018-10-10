package com.fei.repeatplay.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.fei.repeatplay.R;

import java.util.ArrayList;

public class PlayAc extends AppCompatActivity {

    private VideoView play_dis_vv;
    private MediaController mMediaController;

    private ArrayList<String> pathData;
    private int position;

    private boolean isDown = false;
    private float downX = 0;
    private float upX = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_play);

        getDataFromBefor();

        initView();
        initData();
        ininListener();
    }

    private void getDataFromBefor() {
        Intent intent = this.getIntent();
        if (null == intent) {
            return;
        }
        Bundle bundle = intent.getExtras();
        if (null == bundle) {
            return;
        }
        pathData = bundle.getStringArrayList("playList");
        position = bundle.getInt("position", 0);
    }

    private void initView() {
        play_dis_vv = findViewById(R.id.play_dis_vv);
    }

    private void initData() {
        if (null == mMediaController) {
            mMediaController = new MediaController(this);
        }
        if (null == pathData || pathData.size() == 0) {
            Toast.makeText(this, "无法获取视频文件", Toast.LENGTH_SHORT).show();
            return;
        }
        //设置播放视频地址，要提前配置权限
        play_dis_vv.setVideoPath(pathData.get(position));
        //videoview绑定控制器
        play_dis_vv.setMediaController(mMediaController);
        //把控制器视图平铺到哪一个视图上（Mediacontroller底层是一个popupwindow）
        mMediaController.setAnchorView(play_dis_vv);
        play_dis_vv.start();
    }

    private void ininListener() {
        //设置准备完成监听
        play_dis_vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //TODO 视频准备完成后，调用此处方法

            }
        });
        //设置播放完成监听
        play_dis_vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //TODO 视频播放完成后，调用此处方法
                mp.start();
                mp.setLooping(true);
            }
        });

        //设置视频播放出错时候的监听
        play_dis_vv.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                //TODO 视频播放解析出错，调用此处方法
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            isDown = true;
            downX = event.getX();
        }else if (event.getAction() == MotionEvent.ACTION_MOVE){
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            isDown = false;
            upX = event.getX();
            if(upX-downX > 200){
                if(position > 0) {
                    position--;
                }
            }else if(upX - downX < -200){
                if(position < pathData.size()) {
                    position++;
                }
            }
            upX = 0;
            downX = 0;
            initData();
        }

        return super.onTouchEvent(event);
    }
}
