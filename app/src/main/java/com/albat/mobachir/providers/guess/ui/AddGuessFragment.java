package com.albat.mobachir.providers.guess.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.albat.mobachir.BaseFragment;
import com.albat.mobachir.R;
import com.albat.mobachir.network.interfaces.FixturesRetrieved;
import com.albat.mobachir.network.managers.LeaguesManager;
import com.albat.mobachir.network.models.Fixture;
import com.albat.mobachir.network.models.Guess;
import com.albat.mobachir.network.models.MessageEvent;
import com.albat.mobachir.providers.guess.AddGuessAdapter;
import com.albat.mobachir.util.InfiniteRecyclerViewAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class AddGuessFragment extends BaseFragment implements FixturesRetrieved {
    String TAG = "AddGuessFragment";

    private RecyclerView recyclerView;
    private ArrayList<Fixture> fixtures = new ArrayList<>();

    private AddGuessAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_add_guess, null);
        setHasOptionsMenu(true);

        recyclerView = view.findViewById(R.id.list);

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        fixtures = new ArrayList<>();
        adapter = new AddGuessAdapter(fixtures, getContext(), getFragmentManager());
        recyclerView.setAdapter(adapter);

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), mLayoutManager.getOrientation());
//        recyclerView.addItemDecoration(dividerItemDecoration);

        getFixtures();

        return view;
    }


    public void getFixtures() {
        adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_PROGRESS);

        LeaguesManager leaguesManager = new LeaguesManager();
        leaguesManager.getAvailableFixturesForGuessing(this);
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
            if (this.fixtures.isEmpty())
                adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_EMPTY);
            else
                adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_LIST);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGuessAdded(Guess guess) {
        for (Fixture fixture : fixtures) {
            if (fixture.id == guess.fixtureId && fixture.manual == guess.manual) {
                if (guess.winnerBid != null && guess.resultBid != null)
                    fixtures.remove(fixture);
                else if (guess.winnerBid != null)
                    fixture.guessWinnerAvailable = false;
                else if (guess.resultBid != null)
                    fixture.guessResultAvailable = false;

                adapter.notifyDataSetChanged();
                return;
            }
        }
    }
}
