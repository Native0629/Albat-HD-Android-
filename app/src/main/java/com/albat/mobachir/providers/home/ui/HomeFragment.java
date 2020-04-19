package com.albat.mobachir.providers.home.ui;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.albat.mobachir.BaseFragment;
import com.albat.mobachir.R;
import com.albat.mobachir.network.models.MessageEvent;
import com.albat.mobachir.providers.home.HomeViewPagerAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;


public class HomeFragment extends BaseFragment implements View.OnClickListener {
    String TAG = "HomeFragment";

    ViewPager viewPager;
    final int TABS_COUNT = 4;
    ArrayList<ViewGroup> tabs = new ArrayList<>();
    HomeViewPagerAdapter viewPagerAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_home, null);
        setHasOptionsMenu(true);

        for (int i = 1; i <= TABS_COUNT; i++) {
            String buttonID = "tab" + i;
            int resID = getActivity().getResources().getIdentifier(buttonID, "id", getActivity().getPackageName());
            ViewGroup tab = view.findViewById(resID);
            tab.setOnClickListener(this);
            tabs.add(tab);
        }
        initializeViewPager(view);

        return view;
    }

    @Override
    public void onClick(View view) {


        int selectedPosition = 0;
        for (ViewGroup tab : tabs) {
            if (tab.getId() == view.getId()) {
                break;
            }
            selectedPosition++;
        }

        updateTabs(tabs.get(selectedPosition));
        viewPager.setCurrentItem(selectedPosition, true);
    }

    private void updateTabs(View view) {
        final int SELECTED_COLOR = Color.parseColor("#FFE301");
        final int NORMAL_COLOR = Color.WHITE;

        for (ViewGroup tab : tabs) {
            ViewGroup container = (ViewGroup) tab.getChildAt(0);
            ImageView icon = (ImageView) container.getChildAt(0);
            TextView title = (TextView) container.getChildAt(1);
            View selector = (View) container.getChildAt(2);

            if (tab.getId() == view.getId()) {
                icon.setColorFilter(SELECTED_COLOR);
                title.setTextColor(SELECTED_COLOR);
                selector.setVisibility(View.VISIBLE);
            } else {
                icon.setColorFilter(NORMAL_COLOR);
                title.setTextColor(NORMAL_COLOR);
                selector.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void initializeViewPager(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPagerAdapter = new HomeViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(5);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateTabs(tabs.get(position));
                EventBus.getDefault().post(new MessageEvent(MessageEvent.SHOW_INTERSTITIAL_AD));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabs.get(0).performClick();
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
