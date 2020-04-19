package com.albat.mobachir;

import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.albat.mobachir.providers.tv.TvFragment;
import com.albat.mobachir.providers.worldcup.ui.CWebviewFragment;
import com.albat.mobachir.util.Helper;

/**
 * This file is part of the Universal template
 * For license information, please check the LICENSE
 * file in the root of this project
 *
 * @author Sherdle
 * Copyright 2017
 */
public class FullScreenActivity extends AppCompatActivity {
    //Data to pass to a fragment
    public static String LINK = "link";


    String link;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); //Remove title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Remove notification bar

        setContentView(R.layout.activity_fullscreen);

        if (savedInstanceState != null)
            link = savedInstanceState.getString(LINK);
        else
            link = getIntent().getStringExtra(LINK);

        if (link.endsWith("m3u8")) {
            TvFragment tvFragment = new TvFragment();
            Bundle bundle = new Bundle();
            bundle.putStringArray(MainActivity.FRAGMENT_DATA, new String[]{link});
            tvFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.container, tvFragment);
            fragmentTransaction.commitAllowingStateLoss();
        } else {
            CWebviewFragment webviewFragment = new CWebviewFragment();
            Bundle bundle = new Bundle();
            bundle.putStringArray(MainActivity.FRAGMENT_DATA, new String[]{link});
            webviewFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.container, webviewFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }

        Helper.admobLoader(this, findViewById(R.id.adView));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LINK, link);
    }
}



