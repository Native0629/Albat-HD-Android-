package com.albat.mobachir.providers.fixture.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.albat.mobachir.BaseFragment;
import com.albat.mobachir.Config;
import com.albat.mobachir.R;
import com.albat.mobachir.network.interfaces.FixtureRetrieved;
import com.albat.mobachir.network.managers.FixturesManager;
import com.albat.mobachir.network.models.Fixture;
import com.albat.mobachir.network.models.Fixture.MatchStatus;
import com.albat.mobachir.network.models.MessageEvent;
import com.albat.mobachir.providers.fixture.ViewFixtureViewPagerAdapter;
import com.albat.mobachir.util.CalendarHelper;
import com.albat.mobachir.util.SharedPreferencesManager;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewFixtureFragment extends BaseFragment implements FixtureRetrieved {
    String TAG = "ViewFixtureFragment";
    //Views
    private LinearLayout loadingLayout;

    Fixture fixture;

    ViewPager viewPager;
    TabLayout tabLayout;
    ViewFixtureViewPagerAdapter viewFixtureViewPagerAdapter;
    SharedPreferencesManager sharedPreferencesManager;

    boolean now = false;


    CircleImageView team1Picture;
    TextView team1;
    CircleImageView team2Picture;
    TextView team2;
    TextView info;
    TextView team1Result;
    TextView team2Result;

    public ImageView viewBroadcast;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_view_fixture, null);
        setHasOptionsMenu(true);

        sharedPreferencesManager = SharedPreferencesManager.getInstance(getActivity());

        loadingLayout = view.findViewById(R.id.loadingLayout);

        team1Picture = view.findViewById(R.id.team1Picture);
        team1 = view.findViewById(R.id.team1);
        team2Picture = view.findViewById(R.id.team2Picture);
        team2 = view.findViewById(R.id.team2);
        info = view.findViewById(R.id.info);
        team1Result = view.findViewById(R.id.team1Result);
        team2Result = view.findViewById(R.id.team2Result);

        initializeViewPager(view);

        viewBroadcast = view.findViewById(R.id.viewBroadcast);


        loadInfo();

        return view;
    }

    private void loadInfo() {
        if (fixture.broadcast) {
            viewBroadcast.setVisibility(View.VISIBLE);
            viewBroadcast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment;
                    if (fixture.links == null || fixture.links.isEmpty())
                        fragment = new LinksNotAvailableNowFragment().setFixture(fixture);
                    else
                        fragment = new ViewFixtureLinksFragment().setFixture(fixture);
                    FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                    fragmentTransaction.add(R.id.container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.SHOW_INTERSTITIAL_AD));
                }
            });
        } else {
            viewBroadcast.setVisibility(View.GONE);
        }

        team1.setText(fixture.team1);
        team2.setText(fixture.team2);

        team1Result.setVisibility(View.GONE);
        team2Result.setVisibility(View.GONE);

        info.setText(CalendarHelper.reformatDateString(fixture.dateTime, Config.API_DATETIME_FORMAT, Config.APP_TIME_FORMAT, fixture.timezone, false));

        switch (fixture.getStatus()) {
            case MatchStatus.LIVE:
            case MatchStatus.HALF_TIME:
            case MatchStatus.BREAK:
            case MatchStatus.EXTRA_TIME:
            case MatchStatus.PENALITY_LIVE:
                if (!fixture.manual) {
                    team1Result.setVisibility(View.VISIBLE);
                    team2Result.setVisibility(View.VISIBLE);

                    team1Result.setText(fixture.team1Goals + "");
                    team2Result.setText(fixture.team2Goals + "");
                    info.setText(" - ");
                    if (fixture.team1PenGoals != 0 && fixture.team2PenGoals != 0) {
                        team2Result.setText(fixture.team2Goals + " (" + fixture.team2PenGoals + ")");
                        team1Result.setText("(" + fixture.team1PenGoals + ") " + fixture.team1Goals);
                    }
                }
                break;
            case MatchStatus.ENDED_AFTER_FULL_TIME:
            case MatchStatus.ENDED_AFTER_EXTRA_TIME:
            case MatchStatus.ENDED_AFTER_PENALITY_SHOOTOUT:
            case MatchStatus.ENDED:
                if (!fixture.manual || (fixture.manual && fixture.resultAdded)) {
                    team1Result.setVisibility(View.VISIBLE);
                    team2Result.setVisibility(View.VISIBLE);

                    team1Result.setText(fixture.team1Goals + "");
                    team2Result.setText(fixture.team2Goals + "");
                    info.setText(" - ");
                    if (fixture.team1PenGoals != 0 && fixture.team2PenGoals != 0) {
                        team2Result.setText(fixture.team2Goals + " (" + fixture.team2PenGoals + ")");
                        team1Result.setText("(" + fixture.team1PenGoals + ") " + fixture.team1Goals);
                    }
                }
                break;
            case MatchStatus.NOT_STARTED:
            case MatchStatus.POSTPONED:
            case MatchStatus.DELAYED:
            case MatchStatus.CANCELED:
            case MatchStatus.SUSPENDED:
            case MatchStatus.INTERRUPTED:
                break;
        }

        try {
            Picasso.with(getActivity()).load(fixture.team1Picture).error(R.drawable.ic_team_no_image).placeholder(R.drawable.ic_team_no_image).into(team1Picture);
        } catch (Exception e) {
            team1Picture.setImageResource(R.drawable.ic_team_no_image);
        }

        try {
            Picasso.with(getActivity()).load(fixture.team2Picture).error(R.drawable.ic_team_no_image).placeholder(R.drawable.ic_team_no_image).into(team2Picture);
        } catch (Exception e) {
            team2Picture.setImageResource(R.drawable.ic_team_no_image);
        }

        viewFixtureViewPagerAdapter.setFixture(fixture);
    }

    public void getFixture() {
        loadingLayout.setVisibility(View.VISIBLE);
        FixturesManager fixturesManager = new FixturesManager();
        fixturesManager.getFixture(fixture.id, this);
    }

    @Override
    public void onFixtureRetrieved(Fixture fixture, boolean success, String error) {
        loadingLayout.setVisibility(View.GONE);
        if (!success || fixture == null) {
            return;
        }

        this.fixture = fixture;

        loadInfo();
    }

    private void initializeViewPager(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("معلومات"));
        tabLayout.addTab(tabLayout.newTab().setText("التشكيلة"));
        tabLayout.addTab(tabLayout.newTab().setText("الإحصائيات"));
        tabLayout.addTab(tabLayout.newTab().setText("الأحداث"));
        tabLayout.addTab(tabLayout.newTab().setText("التعليقات"));

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewFixtureViewPagerAdapter = new ViewFixtureViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(viewFixtureViewPagerAdapter);
        viewPager.setOffscreenPageLimit(5);


        //tabLayout.addTab(tabLayout.newTab().setText("النقاط"));

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

    public ViewFixtureFragment setFixture(Fixture fixture) {
        this.fixture = fixture;
        return this;
    }

    public ViewFixtureFragment setNow(boolean now) {
        this.now = now;
        return this;
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        switch (messageEvent.getCode()) {
            case MessageEvent.REFRESH:
                getFixture();
                break;
        }
    }
}


