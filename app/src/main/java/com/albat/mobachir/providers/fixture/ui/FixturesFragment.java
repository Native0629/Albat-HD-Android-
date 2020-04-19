package com.albat.mobachir.providers.fixture.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.albat.mobachir.BaseFragment;
import com.albat.mobachir.Config;
import com.albat.mobachir.R;
import com.albat.mobachir.network.interfaces.LeaguesWithFixturesRetrieved;
import com.albat.mobachir.network.managers.LeaguesManager;
import com.albat.mobachir.network.models.Fixture;
import com.albat.mobachir.network.models.League;
import com.albat.mobachir.network.models.MessageEvent;
import com.albat.mobachir.providers.fixture.FixturesAdapter;
import com.albat.mobachir.util.CalendarHelper;
import com.albat.mobachir.util.SharedPreferencesManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class FixturesFragment extends BaseFragment implements LeaguesWithFixturesRetrieved {
    String TAG = "FixturesFragment";

    public enum Mode {ALL, IMPORTANT, FAVORITE}

    Mode mode = Mode.IMPORTANT;

    private LinearLayout noData;
    private LinearLayout loadingLayout;

    private StickyListHeadersListView recyclerView;
    private ArrayList<League> leagues = new ArrayList<>();

    private FixturesAdapter adapter;

    Date date;

    SharedPreferencesManager sharedPreferencesManager;

    boolean isInitialized = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_main_fixtures, null);
        setHasOptionsMenu(true);

        sharedPreferencesManager = SharedPreferencesManager.getInstance(getActivity());

        noData = view.findViewById(R.id.noData);
        loadingLayout = view.findViewById(R.id.loadingLayout);
        recyclerView = view.findViewById(R.id.list);

        leagues = new ArrayList<>();
        adapter = new FixturesAdapter(getContext(), leagues, new FixturesAdapter.OnFixtureClickListener() {
            @Override
            public void onFixtureClicked(Fixture fixture) {
                Fragment fragment = new ViewFixtureFragment().setFixture(fixture);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                EventBus.getDefault().post(new MessageEvent(MessageEvent.SHOW_INTERSTITIAL_AD));
            }
        });
        recyclerView.setAdapter(adapter);

        isInitialized = true;
        if (date != null) {
            getFixtures();
        }

        return view;
    }


    public void getFixtures() {
        loadingLayout.setVisibility(View.VISIBLE);
        String date = CalendarHelper.formatDateEn(this.date, Config.API_DATE_FORMAT);
        LeaguesManager leaguesManager = new LeaguesManager();
        switch (mode) {
            case ALL:
                leaguesManager.getLeagueWithFixtures(date, 0, this);
                break;
            case IMPORTANT:
                leaguesManager.getLeagueWithFixtures(date, 1, this);
                break;
            case FAVORITE:
                leaguesManager.getLeagueWithFixtures(date, 2, sharedPreferencesManager.getFavoriteIds(), this);
        }
    }

    @Override
    public void onLeaguesWithFixturesRetrieved(ArrayList<League> leagues, boolean success, String error) {
        loadingLayout.setVisibility(View.GONE);
        if (!success || leagues == null) {
            return;
        } else {
            //Add all the new posts to the list and notify the adapter
            this.leagues.clear();
            this.leagues.addAll(leagues);

            int fixturesCount = 0;
            for (League league : leagues)
                if (league.fixtures != null) {
                    fixturesCount += league.fixtures.size();
                }

            if (fixturesCount == 0 || leagues.isEmpty())
                noData.setVisibility(View.VISIBLE);
            else
                noData.setVisibility(View.GONE);

            adapter.notifyDataSetChanged();
        }
    }


    public FixturesFragment setMode(Mode mode) {
        this.mode = mode;
        return this;
    }

    public void updateFixtures(Date date) {
        this.date = date;
        if (isInitialized) {
            getFixtures();
        }
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
                getFixtures();
                break;
        }
    }
}