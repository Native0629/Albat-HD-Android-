package com.albat.mobachir.providers.guess.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.albat.mobachir.BaseFragment;
import com.albat.mobachir.R;
import com.albat.mobachir.network.interfaces.LeaderboardRetrieved;
import com.albat.mobachir.network.managers.UserManager;
import com.albat.mobachir.network.models.Leaderboard;
import com.albat.mobachir.network.models.MessageEvent;
import com.albat.mobachir.providers.guess.LeaderboardAdapter;
import com.albat.mobachir.util.InfiniteRecyclerViewAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;

public class LeaderboardFragment extends BaseFragment implements LeaderboardRetrieved {
    String TAG = "LeaderboardFragment";
    //Views
    private RecyclerView recyclerView;
    private ArrayList<Leaderboard> items = new ArrayList<>();

    private final String[] MONTHS = new String[]{"يناير", "فبراير", "مارس", "إبريل", "مايو", "يونيو", "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر"};

    Spinner monthSpinner;
    ArrayAdapter<String> monthsAdapter;
    //List
    private LeaderboardAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_leaderboard, null);
        setHasOptionsMenu(true);
        recyclerView = view.findViewById(R.id.list);

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        items = new ArrayList<>();
        adapter = new LeaderboardAdapter(items, getContext());
        recyclerView.setAdapter(adapter);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        monthSpinner = view.findViewById(R.id.monthsSpinner);
        monthsAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_layout);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        for (int i = 0; i < MONTHS.length; i++) {
            if (i == currentMonth)
                monthsAdapter.add("الشهر الحالي");
            else
                monthsAdapter.add(MONTHS[i]);
        }
        monthsAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthsAdapter);
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                loadItems(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        monthSpinner.setSelection(currentMonth, true);

        //loadItems(currentMonth + 1);

        return view;
    }

    int selectedMonthNumber = 0;

    public void loadItems(int monthNumber) {
        selectedMonthNumber = monthNumber;

        adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_PROGRESS);

        UserManager userManager = new UserManager();
        userManager.getLeaderboard(monthNumber, this);
    }

    @Override
    public void onLeaderboardRetrieved(ArrayList<Leaderboard> leaderboard, boolean success, String error) {
        if (!success || leaderboard == null) {
            adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_EMPTY);
            return;
        } else {
            //Add all the new posts to the list and notify the adapter
            items.clear();
            items.addAll(leaderboard);
            if (items.isEmpty())
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
                loadItems(selectedMonthNumber);
                break;
        }
    }
}
