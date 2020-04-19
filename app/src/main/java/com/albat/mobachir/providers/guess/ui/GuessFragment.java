package com.albat.mobachir.providers.guess.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.albat.mobachir.App;
import com.albat.mobachir.BaseFragment;
import com.albat.mobachir.LoginActivity;
import com.albat.mobachir.R;
import com.albat.mobachir.network.models.MessageEvent;
import com.albat.mobachir.providers.guess.GuessViewPagerAdapter;
import com.albat.mobachir.util.DialogHelper;
import com.albat.mobachir.util.SharedPreferencesManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class GuessFragment extends BaseFragment {
    String TAG = "GuessFragment";

    DialogHelper dialogHelper;
    SharedPreferencesManager sharedPreferencesManager;
    App app;

    LinearLayout signinLayout;

    ViewPager viewPager;
    TabLayout tabLayout;
    GuessViewPagerAdapter viewPagerAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_guess, null);
        setHasOptionsMenu(true);


        dialogHelper = new DialogHelper();
        sharedPreferencesManager = SharedPreferencesManager.getInstance(getActivity());
        app = App.getInstance();

        signinLayout = view.findViewById(R.id.signinLayout);
        view.findViewById(R.id.signinButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        if (!sharedPreferencesManager.isLoggedIn()) {
            signinLayout.setVisibility(View.VISIBLE);
            return view;
        } else {
            signinLayout.setVisibility(View.GONE);
        }


//        view.findViewById(R.id.prizeList).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Fragment fragment = new PrizeListFragment();
//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                fragmentTransaction.add(R.id.container, fragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commitAllowingStateLoss();
//                //startActivity(new Intent(getActivity(), LoginActivity.class));
//            }
//        });
        view.findViewById(R.id.watchAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                DialogFragment dialogFragment = new AddCoinsDialog();
                dialogFragment.show(ft, "dialog");
            }
        });

        initializeViewPager(view);

        return view;
    }

    private void initializeViewPager(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPagerAdapter = new GuessViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(5);

        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("توقع"));
        tabLayout.addTab(tabLayout.newTab().setText("الجوائز"));
        tabLayout.addTab(tabLayout.newTab().setText("المتصدرين"));
        tabLayout.addTab(tabLayout.newTab().setText("توقعاتي"));
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

        tabLayout.getTabAt(0).select();
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
            case MessageEvent.UPDATE_COINS:
                break;
        }
    }

    @Override
    public boolean popBackStack() {
        FragmentPagerAdapter adapter = (FragmentPagerAdapter) viewPager.getAdapter();
        BaseFragment fragment = ((BaseFragment) adapter.getItem(viewPager.getCurrentItem()));
        return fragment.popBackStack();
    }
}
