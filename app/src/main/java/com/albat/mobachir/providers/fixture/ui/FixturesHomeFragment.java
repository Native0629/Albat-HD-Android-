package com.albat.mobachir.providers.fixture.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.albat.mobachir.App;
import com.albat.mobachir.BaseFragment;
import com.albat.mobachir.Config;
import com.albat.mobachir.R;
import com.albat.mobachir.network.models.MessageEvent;
import com.albat.mobachir.providers.fixture.FixtureViewPagerAdapter;
import com.albat.mobachir.util.CalendarHelper;
import com.albat.mobachir.util.DialogHelper;
import com.albat.mobachir.util.SharedPreferencesManager;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;


public class FixturesHomeFragment extends BaseFragment {
    String TAG = "FixturesHomeFragment";

    DialogHelper dialogHelper;
    SharedPreferencesManager sharedPreferencesManager;
    App app;

    ViewPager viewPager;
    TabLayout tabLayout;
    FixtureViewPagerAdapter viewPagerAdapter;

    TextView dateLabel, dayLabel;

    Date date;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_fixtures_home, null);
        setHasOptionsMenu(true);

        date = Calendar.getInstance().getTime();

        dateLabel = view.findViewById(R.id.date);
        dayLabel = view.findViewById(R.id.dayLabel);

        initializeViewPager(view);
        updateDate();

        view.findViewById(R.id.nextDay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = CalendarHelper.addDays(date, 1);
                updateDate();
            }
        });
        view.findViewById(R.id.prevDay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = CalendarHelper.addDays(date, -1);
                updateDate();
            }
        });

        dialogHelper = new DialogHelper();
        sharedPreferencesManager = SharedPreferencesManager.getInstance(getActivity());
        app = App.getInstance();


        return view;
    }

    private void updateDate() {
        dateLabel.setText(CalendarHelper.formatDate(date, Config.APP_FULL_DATE_FORMAT, false));
        if (CalendarHelper.isToday(date))
            dayLabel.setText("اليوم");
        else
            dayLabel.setText(CalendarHelper.formatDate(date, "EEEE"));

        viewPagerAdapter.requestFixtures(date);
    }


    private void initializeViewPager(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPagerAdapter = new FixtureViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(5);

        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("الكل"));
        tabLayout.addTab(tabLayout.newTab().setText("الأقوي"));
        tabLayout.addTab(tabLayout.newTab().setText("المفضلة"));
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

        tabLayout.getTabAt(1).select();
    }

    @Override
    public boolean popBackStack() {
        if (super.popBackStack())
            return true;

        FragmentPagerAdapter adapter = (FragmentPagerAdapter) viewPager.getAdapter();
        BaseFragment fragment = ((BaseFragment) adapter.getItem(viewPager.getCurrentItem()));
        return fragment.popBackStack();

    }
}
