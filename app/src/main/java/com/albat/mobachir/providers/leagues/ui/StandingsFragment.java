package com.albat.mobachir.providers.leagues.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.albat.mobachir.BaseFragment;
import com.albat.mobachir.R;
import com.albat.mobachir.network.interfaces.StagesRetrieved;
import com.albat.mobachir.network.managers.LeaguesManager;
import com.albat.mobachir.network.models.League;
import com.albat.mobachir.network.models.MessageEvent;
import com.albat.mobachir.network.models.Stage;
import com.albat.mobachir.providers.leagues.StandingsAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class StandingsFragment extends BaseFragment implements StagesRetrieved {
    String TAG = "StandingsFragment";

    private LinearLayout noData;
    //Views
    private StickyListHeadersListView recyclerView;
    private ArrayList<Stage> items = new ArrayList<>();

    //List
    private StandingsAdapter adapter;

    private League league;

    private boolean isInitialized = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_standings, null);
        setHasOptionsMenu(true);

        noData = view.findViewById(R.id.noData);
        recyclerView = view.findViewById(R.id.list);

        //Load items
        isInitialized = true;
        if (league != null) {
            loadItems();
        }

        return view;
    }

    public void loadItems() {
        noData.setVisibility(View.GONE);
        LeaguesManager leaguesManager = new LeaguesManager();
        leaguesManager.getStagesWithStandings(league.activeSeasonId, this);
    }

    @Override
    public void onStagesRetrieved(ArrayList<Stage> stages, boolean success, String error) {
        if (!success || stages == null) {
            noData.setVisibility(View.VISIBLE);
            return;
        } else {
            int standingsCount = 0;
            for (Stage stage : stages)
                if (stage.standings != null) {
                    standingsCount += stage.standings.size();
                }

            if (standingsCount == 0 || stages.isEmpty())
                noData.setVisibility(View.VISIBLE);
            else
                noData.setVisibility(View.GONE);

            //Add all the new posts to the list and notify the adapter
            items.clear();
            items.addAll(stages);
            adapter = new StandingsAdapter(getActivity(), items);
            if (!stages.isEmpty()) {
                if (stages.size() == 1 & stages.get(0).enName.equalsIgnoreCase("Regular Season")) {
                    recyclerView.getWrappedList().setAdapter(adapter); // This is the main point
                } else
                    recyclerView.setAdapter(adapter);
            }
            adapter.notifyDataSetChanged();
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
