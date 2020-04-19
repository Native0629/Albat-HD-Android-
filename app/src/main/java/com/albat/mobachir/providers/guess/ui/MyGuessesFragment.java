package com.albat.mobachir.providers.guess.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.albat.mobachir.App;
import com.albat.mobachir.BaseFragment;
import com.albat.mobachir.MainActivity;
import com.albat.mobachir.R;
import com.albat.mobachir.network.interfaces.GuessesRetrieved;
import com.albat.mobachir.network.managers.UserManager;
import com.albat.mobachir.network.models.Guess;
import com.albat.mobachir.network.models.MessageEvent;
import com.albat.mobachir.providers.leagues.GuessAdapter;
import com.albat.mobachir.util.DialogHelper;
import com.albat.mobachir.util.InfiniteRecyclerViewAdapter;
import com.albat.mobachir.util.SharedPreferencesManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;


public class MyGuessesFragment extends BaseFragment implements GuessesRetrieved {
    String TAG = "MyGuessesFragment";
    //Views
    private FloatingActionButton refreshFab;
    private ArrayList<Guess> items = new ArrayList<>();

    private LinearLayout noData;

    DialogHelper dialogHelper;

    SharedPreferencesManager sharedPreferencesManager;
    App app;

    private RecyclerView recyclerView;
    private GuessAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_my_guesses, null);
        setHasOptionsMenu(true);

        noData = view.findViewById(R.id.noData);

        dialogHelper = new DialogHelper();
        sharedPreferencesManager = SharedPreferencesManager.getInstance(getActivity());
        app = App.getInstance();

        recyclerView = view.findViewById(R.id.list);

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        items = new ArrayList<>();
        adapter = new GuessAdapter(items, getContext());
        recyclerView.setAdapter(adapter);
        adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_PROGRESS);

        refreshFab = view.findViewById(R.id.refresh);
        refreshFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).showInterstitial();
                }

                refreshFab.setVisibility(View.GONE);
                adapter.clear();
                adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_PROGRESS);
                loadItems();
            }
        });

        //Load items
        loadItems();

        return view;
    }


    public void loadItems() {
        noData.setVisibility(View.GONE);
        adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_PROGRESS);

        UserManager userManager = new UserManager();
        userManager.getGuesses(this);
    }

    @Override
    public void onGuessesRetrieved(ArrayList<Guess> guesses, boolean success, String error) {
        refreshFab.setVisibility(View.VISIBLE);

        if (!success || guesses == null) {
            Toast.makeText(getActivity(), R.string.invalid_configuration, Toast.LENGTH_LONG).show();
            adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_EMPTY);
            return;
        }

        //Add all the new posts to the list and notify the adapter
        items.clear();
        items.addAll(guesses);
        if (items.isEmpty()) {
            noData.setVisibility(View.VISIBLE);
            adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_EMPTY);
        } else {
            noData.setVisibility(View.GONE);
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
                loadItems();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGuessAdded(Guess guess) {
        boolean found = false;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).id == guess.id) {
                items.set(i, guess);
                found = true;
                break;
            }
        }
        if (!found)
            items.add(0, guess);
        adapter.notifyDataSetChanged();
        noData.setVisibility(View.GONE);
    }
}
