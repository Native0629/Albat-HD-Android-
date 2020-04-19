package com.albat.mobachir;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.albat.mobachir.util.Helper;

import tcking.github.com.giraffeplayer2.VideoView;


/**
 * This file is part of the Universal template
 * For license information, please check the LICENSE
 * file in the root of this project
 *
 * @author Sherdle
 * Copyright 2017
 */
public class VideoPlayerActivity extends AppCompatActivity {
    String TAG = "VideoPlayerActivity";
    //Data to pass to a fragment
    public static String LINK = "link";


    String link;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); //Remove title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Remove notification bar

        setContentView(R.layout.activity_video_player);

        if (savedInstanceState != null)
            link = savedInstanceState.getString(LINK);
        else
            link = getIntent().getStringExtra(LINK);

        VideoView videoView = (VideoView) findViewById(R.id.video_view);

        videoView.setVideoPath(link).getPlayer().start();
        Helper.admobLoader(this, findViewById(R.id.adView));
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LINK, link);
    }
}



