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

import com.albat.mobachir.BaseFragment;
import com.albat.mobachir.MainActivity;
import com.albat.mobachir.R;
import com.albat.mobachir.network.interfaces.PrizesRetrieved;
import com.albat.mobachir.network.managers.UserManager;
import com.albat.mobachir.network.models.Prize;
import com.albat.mobachir.providers.guess.PrizesAdapter;
import com.albat.mobachir.util.InfiniteRecyclerViewAdapter;

import java.util.ArrayList;

public class PrizeListFragment extends BaseFragment implements PrizesRetrieved {
    String TAG = "PrizeListFragment";
    //Views
    private FloatingActionButton refreshFab;
    private RecyclerView recyclerView;
    private ArrayList<Prize> items = new ArrayList<>();

    //List
    private PrizesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_prize_list, null);
        setHasOptionsMenu(true);
        recyclerView = view.findViewById(R.id.list);

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        items = new ArrayList<>();
        adapter = new PrizesAdapter(items, getContext());
        recyclerView.setAdapter(adapter);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        refreshFab = view.findViewById(R.id.refresh);
        refreshFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).showInterstitial();
                }

                refreshFab.setVisibility(View.GONE);
                adapter.clear();
                loadItems();
            }
        });

        loadItems();

        return view;
    }

    public void loadItems() {
        adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_PROGRESS);

        UserManager userManager = new UserManager();
        userManager.getPrizes(this);
    }

    @Override
    public void onPrizesRetrieved(ArrayList<Prize> prizes, boolean success, String error) {
        refreshFab.setVisibility(View.VISIBLE);

        if (!success || prizes == null) {
            adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_EMPTY);
            return;
        } else {
            //Add all the new posts to the list and notify the adapter
            items.clear();
            items.addAll(prizes);
            if (items.isEmpty())
                adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_EMPTY);
            else
                adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_LIST);
        }
    }
}
