package com.albat.mobachir.providers.fixture.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.albat.mobachir.BaseFragment;
import com.albat.mobachir.R;
import com.albat.mobachir.network.interfaces.LeaguesWithFixturesRetrieved;
import com.albat.mobachir.network.managers.LeaguesManager;
import com.albat.mobachir.network.models.Fixture;
import com.albat.mobachir.network.models.League;
import com.albat.mobachir.network.models.MessageEvent;
import com.albat.mobachir.providers.fixture.FixturesAdapter;
import com.albat.mobachir.util.SharedPreferencesManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class FixturesNowFragment extends BaseFragment implements LeaguesWithFixturesRetrieved {
    String TAG = "FixturesFragment";

    private LinearLayout noData;
    private LinearLayout loadingLayout;

    private StickyListHeadersListView recyclerView;
    private ArrayList<League> leagues = new ArrayList<>();

    private FixturesAdapter adapter;

    SharedPreferencesManager sharedPreferencesManager;

    FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_main_fixtures, null);
        setHasOptionsMenu(true);

        sharedPreferencesManager = SharedPreferencesManager.getInstance(getActivity());

        noData = view.findViewById(R.id.noData);
        loadingLayout = view.findViewById(R.id.loadingLayout);
        recyclerView = view.findViewById(R.id.list);

        if (fragmentManager == null)
            fragmentManager = getChildFragmentManager();

        leagues = new ArrayList<>();
        adapter = new FixturesAdapter(getContext(), leagues, new FixturesAdapter.OnFixtureClickListener() {
            @Override
            public void onFixtureClicked(Fixture fixture) {
                Fragment fragment = new ViewFixtureFragment().setFixture(fixture);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                EventBus.getDefault().post(new MessageEvent(MessageEvent.SHOW_INTERSTITIAL_AD));
            }
        });
        recyclerView.setAdapter(adapter);

        getFixtures();

        return view;
    }


    public void getFixtures() {
        loadingLayout.setVisibility(View.VISIBLE);
        LeaguesManager leaguesManager = new LeaguesManager();
        leaguesManager.getLeagueWithFixturesNow(this);
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