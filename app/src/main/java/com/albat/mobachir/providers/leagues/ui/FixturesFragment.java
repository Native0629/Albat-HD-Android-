package com.albat.mobachir.providers.leagues.ui;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.albat.mobachir.BaseFragment;
import com.albat.mobachir.R;
import com.albat.mobachir.network.interfaces.FixturesRetrieved;
import com.albat.mobachir.network.interfaces.StagesRetrieved;
import com.albat.mobachir.network.managers.LeaguesManager;
import com.albat.mobachir.network.models.Fixture;
import com.albat.mobachir.network.models.League;
import com.albat.mobachir.network.models.MessageEvent;
import com.albat.mobachir.network.models.Stage;
import com.albat.mobachir.providers.leagues.FixturesAdapter;
import com.albat.mobachir.util.InfiniteRecyclerViewAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class FixturesFragment extends BaseFragment implements StagesRetrieved, FixturesRetrieved {
    String TAG = "FixturesFragment";

    private LinearLayout noData;
    private RecyclerView recyclerView;
    private ArrayList<Fixture> fixtures = new ArrayList<>();
    private ArrayList<Stage> stages = new ArrayList<>();

    private FixturesAdapter adapter;

    private League league;

    Spinner stageSpinner;
    ArrayAdapter<String> stagesAdapter;

    private boolean isInitialized = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_fixtures, null);
        setHasOptionsMenu(true);

        noData = view.findViewById(R.id.noData);

        stageSpinner = view.findViewById(R.id.stageSpinner);
        stagesAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_layout);
        stagesAdapter.add("اختر جولة");
        stagesAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        stageSpinner.setAdapter(stagesAdapter);
        stageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                int color = Color.BLACK;
                Stage stage = null;
                if (position == 0 && stages.isEmpty())
                    color = ContextCompat.getColor(getActivity(), R.color.light_gray);
                else {
                    stage = stages.get(position);
                    color = Color.BLACK;
                }
                try {
                    ((TextView) parent.getChildAt(0)).setTextColor(color);
                } catch (Exception e) {

                }
                onStageSelected(stage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        recyclerView = view.findViewById(R.id.list);

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        fixtures = new ArrayList<>();
        adapter = new FixturesAdapter(fixtures, getContext());
        recyclerView.setAdapter(adapter);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        //Load fixtures
        isInitialized = true;
        if (league != null) {
            loadStages();
        }

        return view;
    }

    public void loadStages() {
        adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_PROGRESS);

        LeaguesManager leaguesManager = new LeaguesManager();
        leaguesManager.getStages(league.activeSeasonId, this);
    }

    @Override
    public void onStagesRetrieved(ArrayList<Stage> stages, boolean success, String error) {
        if (!success || stages == null) {
            adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_EMPTY);
            return;
        }

        this.stages = stages;
        stagesAdapter.clear();
        if (stages.isEmpty())
            stagesAdapter.add("اختر جولة");
        for (Stage stage : stages)
            stagesAdapter.add(stage.arName + (league.activeStageId == stage.id ? " (الحالية)" : ""));
        stagesAdapter.notifyDataSetChanged();

        adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_LIST);

        if (!stages.isEmpty()) {
            int index = 0;
            int i = 0;
            for (Stage stage : stages) {
                if (league.activeStageId == stage.id) {
                    index = i;
                    break;
                }
                i++;
            }
            stageSpinner.setSelection(index, true);
            if (stages.size() == 1 && stages.get(0).enName.equalsIgnoreCase("Regular Season"))
                stageSpinner.setVisibility(View.GONE);
            else
                stageSpinner.setVisibility(View.VISIBLE);
            onStageSelected(stages.get(index));
        }
//        if (league.activeStageId != 0) {
//            for (Stage stage : stages)
//                if (stage.id == league.activeStageId) {
//                    onStageSelected(stage);
//                    break;
//                }
//        }
    }


    private void onStageSelected(Stage stage) {
        if (stage != null) {
            getFixtures(stage.id);
        }
    }

    public void getFixtures(int stageId) {
        adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_PROGRESS);
        noData.setVisibility(View.GONE);

        LeaguesManager leaguesManager = new LeaguesManager();
        leaguesManager.getStageFixtures(stageId, this);
    }

    @Override
    public void onFixturesRetrieved(ArrayList<Fixture> fixtures, boolean success, String error) {
        if (!success || fixtures == null) {
            adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_EMPTY);
            return;
        } else {
            //Add all the new posts to the list and notify the adapter
            this.fixtures.clear();
            this.fixtures.addAll(fixtures);
            if (this.fixtures.isEmpty()) {
                noData.setVisibility(View.VISIBLE);
                adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_EMPTY);
            } else {
                noData.setVisibility(View.GONE);
                adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_LIST);
            }
        }
    }

    public Fragment setLeague(League league) {
        this.league = league;
        if (isInitialized && league != null) {
            loadStages();
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
                loadStages();
                break;
        }
    }
}
