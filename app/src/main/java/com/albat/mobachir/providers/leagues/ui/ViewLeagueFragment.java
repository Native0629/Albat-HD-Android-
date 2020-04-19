package com.albat.mobachir.providers.leagues.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.albat.mobachir.BaseFragment;
import com.albat.mobachir.R;
import com.albat.mobachir.network.models.League;
import com.albat.mobachir.network.models.MessageEvent;
import com.albat.mobachir.providers.leagues.LeagueViewPagerAdapter;
import com.albat.mobachir.util.DialogHelper;
import com.albat.mobachir.util.SharedPreferencesManager;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

public class ViewLeagueFragment extends BaseFragment {
    String TAG = "ViewLeagueFragment";
    //Views
    DialogHelper dialogHelper;

    League league;

    ViewPager viewPager;
    TabLayout tabLayout;
    LeagueViewPagerAdapter leagueViewPagerAdapter;
    SharedPreferencesManager sharedPreferencesManager;

    TextView leagueName;
    ImageView leaguePicture;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_view_league, null);
        setHasOptionsMenu(true);

        dialogHelper = new DialogHelper();
        sharedPreferencesManager = SharedPreferencesManager.getInstance(getActivity());

        initializeViewPager(view);

        leagueViewPagerAdapter.setLeague(league);

        leagueName = view.findViewById(R.id.leagueName);
        leaguePicture = view.findViewById(R.id.leaguePicture);

        leagueName.setText(league.arName);
        try {
            Picasso.with(getActivity()).load(league.picture).error(R.drawable.ic_league).placeholder(R.drawable.ic_league).into(leaguePicture);
        } catch (Exception e) {
            leaguePicture.setImageResource(R.drawable.ic_league);
        }

        return view;
    }

    private void initializeViewPager(View view) {
        int count = 3;
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        if (league.rss != null && !league.rss.isEmpty()) {
            tabLayout.addTab(tabLayout.newTab().setText("أخبار متعلقة"));
            count = 4;
        }

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        leagueViewPagerAdapter = new LeagueViewPagerAdapter(getChildFragmentManager(), count);
        viewPager.setAdapter(leagueViewPagerAdapter);
        viewPager.setOffscreenPageLimit(5);


        tabLayout.addTab(tabLayout.newTab().setText("المباريات"));
        tabLayout.addTab(tabLayout.newTab().setText("النقاط"));
        tabLayout.addTab(tabLayout.newTab().setText("الهدافين"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab LayoutTab) {
                viewPager.setCurrentItem(LayoutTab.getPosition());
                EventBus.getDefault().post(new MessageEvent(MessageEvent.SHOW_INTERSTITIAL_AD));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab LayoutTab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab LayoutTab) {

            }
        });
    }

    public ViewLeagueFragment setLeague(League league) {
        this.league = league;
        return this;
    }
}


