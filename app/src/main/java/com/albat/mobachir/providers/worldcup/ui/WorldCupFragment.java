package com.albat.mobachir.providers.worldcup.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.albat.mobachir.MainActivity;
import com.albat.mobachir.R;
import com.albat.mobachir.inherit.BackPressFragment;
import com.albat.mobachir.providers.worldcup.MatchItem;
import com.albat.mobachir.providers.worldcup.MatchesAdapter;
import com.albat.mobachir.providers.worldcup.WorldCupParser;
import com.albat.mobachir.util.Helper;
import com.albat.mobachir.util.InfiniteRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * This file is part of the Universal template
 * For license information, please check the LICENSE
 * file in the root of this project
 *
 * @author Sherdle
 * Copyright 2017
 */
public class WorldCupFragment extends Fragment implements BackPressFragment, MatchesAdapter.OnOverViewClick {
    String TAG = "WorldCupFragment";
    //Views
    private RelativeLayout rl;
    private RecyclerView mRecyclerView;
    private FloatingActionButton refreshFab;
    private ArrayList<MatchItem> items;

    private String matchesUrl;
    private DividerItemDecoration horizontalDec;

    //List
    private MatchesAdapter matchesItemAdapter;

    private ViewTreeObserver.OnGlobalLayoutListener recyclerListener;

    private BroadcastReceiver refreshRequestReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rl = (RelativeLayout) inflater.inflate(R.layout.fragment_worldcup, null);
        setHasOptionsMenu(true);
        mRecyclerView = rl.findViewById(R.id.list);
        mRecyclerView.setBackgroundColor(Color.parseColor("#F9F9F9"));

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        items = new ArrayList<>();
        matchesItemAdapter = new MatchesAdapter(items, getContext(), WorldCupFragment.this);
        mRecyclerView.setAdapter(matchesItemAdapter);
        matchesItemAdapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_PROGRESS);

        if (this.getArguments() != null && this.getArguments().getStringArray(MainActivity.FRAGMENT_DATA) != null && this.getArguments().getStringArray(MainActivity.FRAGMENT_DATA).length != 0)
            matchesUrl = this.getArguments().getStringArray(MainActivity.FRAGMENT_DATA)[0];

        horizontalDec = new DividerItemDecoration(mRecyclerView.getContext(),
                DividerItemDecoration.HORIZONTAL);

        DividerItemDecoration verticalDec = new DividerItemDecoration(mRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(horizontalDec);

        refreshFab = rl.findViewById(R.id.refresh);
        refreshFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).showInterstitial();
                }

                refreshFab.setVisibility(View.GONE);
                matchesItemAdapter.clear();
                matchesItemAdapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_PROGRESS);
                loadItems();
            }
        });

        //Load items
        loadItems();

        return rl;
    }

    public void loadItems() {
        new WorldCupParser(matchesUrl, getActivity(), new WorldCupParser.CallBack() {
            @Override
            public void matchesLoaded(ArrayList<MatchItem> result, boolean failed) {
                if (failed) {
                    //If it failed; show an error if we're using a local file, or if we are online & using a remote overview.
                    if (matchesUrl == null || !matchesUrl.contains("http") || Helper.isOnlineShowDialog(getActivity())) {
                        Toast.makeText(getActivity(), R.string.invalid_configuration, Toast.LENGTH_LONG).show();
                        matchesItemAdapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_EMPTY);
                    }
                } else {
                    //Add all the new posts to the list and notify the adapter
                    items.addAll(result);
                    matchesItemAdapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_LIST);
                }
                refreshFab.setVisibility(View.VISIBLE);
            }
        }).execute();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(recyclerListener);
    }

    @Override
    public void onOverViewSelected(MatchItem item) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showInterstitial();
        }

        Fragment fragment = item.getLinks() == null || item.getLinks().isEmpty() ? new MatchFragment().setMatchItem(item) : new LinksFragment().setMatchItem(item).setMatchesUrl(matchesUrl);
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public boolean handleBackPress() {
        if (getChildFragmentManager().getBackStackEntryCount() > 0) {
            getChildFragmentManager().popBackStack();
            return true;
        }

        return false;
    }
}
