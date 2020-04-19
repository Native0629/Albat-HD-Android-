package com.albat.mobachir.providers.leagues.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.albat.mobachir.BaseFragment;
import com.albat.mobachir.R;
import com.albat.mobachir.network.interfaces.TopScorersRetrieved;
import com.albat.mobachir.network.managers.LeaguesManager;
import com.albat.mobachir.network.models.League;
import com.albat.mobachir.network.models.MessageEvent;
import com.albat.mobachir.network.models.TopScorer;
import com.albat.mobachir.providers.leagues.TopScorerAdapter;
import com.albat.mobachir.util.InfiniteRecyclerViewAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class TopScorerFragment extends BaseFragment implements TopScorersRetrieved {
    String TAG = "TopScorerFragment";
    //Views
    private RecyclerView recyclerView;
    private ArrayList<TopScorer> items = new ArrayList<>();

    //List
    private TopScorerAdapter adapter;

    private League league;

    private LinearLayout notAvailable;

    private boolean isInitialized = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_topscorer, null);
        setHasOptionsMenu(true);

        notAvailable = view.findViewById(R.id.notAvailable);

        recyclerView = view.findViewById(R.id.list);

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        items = new ArrayList<>();
        adapter = new TopScorerAdapter(items, getContext());
        recyclerView.setAdapter(adapter);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        //Load items
        isInitialized = true;
        if (league != null) {
            loadItems();
        }

        return view;
    }

    public void loadItems() {
        if (!league.topScorer) {
            notAvailable.setVisibility(View.VISIBLE);
            return;
        } else
            notAvailable.setVisibility(View.GONE);

        adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_PROGRESS);

        LeaguesManager leaguesManager = new LeaguesManager();
        leaguesManager.getTopScorers(league.activeSeasonId, this);
    }

    @Override
    public void onTopScorersRetrieved(ArrayList<TopScorer> topScorers, boolean success, String error) {
        if (!success || topScorers == null) {
            adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_EMPTY);
            return;
        } else {
            //Add all the new posts to the list and notify the adapter
            items.clear();
            items.addAll(topScorers);
            if (items.isEmpty())
                adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_EMPTY);
            else
                adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_LIST);
        }
    }

    public Fragment setLeague(League league) {
        this.league = league;
        if (isInitialized && league != null) {
            loadItems();
        }
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
                loadItems();
                break;
        }
    }
}
