package com.example.p_viedoview_login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.VideoView;

import static android.media.MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START;

public class LoginActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private VideoView videoBG;
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;
    private ConstraintLayout loginContainer,loginParentContainer;

    private SurfaceView surfaceView;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.v("hank","handleMessage:" + msg.what);
            switch (msg.what){
                case 0:

                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        setVideoView();
    }

    private void init() {
        videoBG = findViewById(R.id.videoView);
        loginContainer = findViewById(R.id.loginContainer);
        loginParentContainer = findViewById(R.id.loginParentContainer);
        surfaceView = findViewById(R.id.surfaceView);

        surfaceView.getHolder().addCallback(this);
    }

    private void setVideoView() {
        // Build your video Uri
        Uri uri = Uri.parse("android.resource://" // First start with this,
                + getPackageName() // then retrieve your package name,
                + "/" // add a slash,
                + R.raw.nba); // and then finally add your video resource. Make sure it is stored
        // in the raw folder.

        // Set the new Uri to our VideoView
        videoBG.setVideoURI(uri);
        // Start the VideoView
        videoBG.start();
        // Set an OnPreparedListener for our VideoView. For more information about VideoViews,
        // check out the Android Docs: https://developer.android.com/reference/android/widget/VideoView.html
        videoBG.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoBG.setBackgroundColor(Color.TRANSPARENT);
                loginParentContainer.setBackground(getResources().getDrawable(R.drawable.bg2));
                mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {

                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        if (what == MEDIA_INFO_VIDEO_RENDERING_START)
                            Log.v("hank", "onInfo => /what:" + what + "start" + MEDIA_INFO_VIDEO_RENDERING_START);
//                        loginContainer.setBackgroundColor(Color.BLACK);
//                        videoBG.setBackgroundColor(Color.BLACK);
//                        loginParentContainer.setBackgroundColor(Color.BLACK);

                        return true;
                    }
                });
                mMediaPlayer = mediaPlayer;
                // We want our video to play over and over so we set looping to true.
                mMediaPlayer.setLooping(true);
                // We then seek to the current posistion if it has been set and play the video.
                if (mCurrentVideoPosition != 0) {
                    mMediaPlayer.seekTo(mCurrentVideoPosition);
                    mMediaPlayer.start();
                }
                Log.v("hank","onPrepared");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Capture the current video position and pause the video.
        mCurrentVideoPosition = mMediaPlayer.getCurrentPosition();
        videoBG.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restart the video when resuming the Activity
        videoBG.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // When the Activity is destroyed, release our MediaPlayer and set it to null.
        mMediaPlayer.release();
        mMediaPlayer = null;
    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.v("hank","surfaceCreated:");
        mMediaPlayer.setDisplay(holder);//视频显示在SurfaceView上
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//媒体声音类型
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
        mHandler.post( new Thread(new Runnable() {
            @Override
            public void run() {
                while (mMediaPlayer.isPlaying()){
                    Log.v("hank","Thread=> +  isPlaying " + mMediaPlayer.getDuration() +"md_現在位置："+ mMediaPlayer.getCurrentPosition() );

                    if (mMediaPlayer.getDuration()-mMediaPlayer.getCurrentPosition()<=1000){
                        mMediaPlayer.seekTo(0);
                        Log.v("hank","Thread=> +  mp_Duration " + mMediaPlayer.getDuration() +"md_現在位置："+ mMediaPlayer.getCurrentPosition() );
                    }
                }
            }
        }));
        mHandler.sendEmptyMessage(0);
        //Log.d(TAG,"视频总时长:"+mp.getDuration());



    }



    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
