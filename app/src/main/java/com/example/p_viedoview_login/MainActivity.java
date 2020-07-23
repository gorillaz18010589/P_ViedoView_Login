package com.example.p_viedoview_login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private VideoView videoBG;
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;

    private Button btnToLogin,btLogInButton;
    private EditText etEmailAddress,etPassword;
    private ImageView imageView;
    private TextView transition6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hook up the VideoView to our UI.
        init();
        setVideoView();

    }

    private void init() {
        btnToLogin = findViewById(R.id.btnToLogin);
        videoBG = (VideoView) findViewById(R.id.videoView);
        btnToLogin.setOnClickListener(this);

        btLogInButton = findViewById(R.id.btLogInButton);
        etEmailAddress = findViewById(R.id.etEmailAddress);
        etPassword = findViewById(R.id.etPassword);
        imageView = findViewById(R.id.imageView);

        transition6 = findViewById(R.id.transition6);
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
                mMediaPlayer = mediaPlayer;
                // We want our video to play over and over so we set looping to true.
                mMediaPlayer.setLooping(true);
                // We then seek to the current posistion if it has been set and play the video.
                if (mCurrentVideoPosition != 0) {
                    mMediaPlayer.seekTo(mCurrentVideoPosition);
                    mMediaPlayer.start();
                }
            }
        });
    }

    /*================================ Important Section! ================================
    We must override onPause(), onResume(), and onDestroy() to properly handle our
    VideoView.
     */

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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnToLogin:
                setAnimation();
                break;
        }
    }

    private void setAnimation() {
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);

        //3.準備ActivityOptions.makeSceneTransitionAnimation方法,製造綁定特效果
        Pair[] pairs = new Pair[6];
        pairs[0]  = new Pair<View,String>(imageView,"transition1");
        pairs[1]  = new Pair<View,String>(etEmailAddress,"transitio2");
        pairs[2]  = new Pair<View,String>(etPassword,"transitio3");
        pairs[3]  = new Pair<View,String>(btLogInButton,"transitio4");
        pairs[4]  = new Pair<View,String>(btnToLogin,"transitio5");
        pairs[5]  = new Pair<View,String>(transition6,"transition6");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//                    getWindow().setEnterTransition(new Slide().setDuration(001));
//                    getWindow().setExitTransition(new Slide().setDuration(001));
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
            startActivity(intent,activityOptions.toBundle());

        }else{
            Log.v("hank","小於的版本沒效果");
        }
    }
}
