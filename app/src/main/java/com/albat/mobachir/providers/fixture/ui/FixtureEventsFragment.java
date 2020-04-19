package com.albat.mobachir.providers.fixture.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.albat.mobachir.BaseFragment;
import com.albat.mobachir.R;
import com.albat.mobachir.network.models.Fixture;
import com.albat.mobachir.providers.fixture.EventsAdapter;
import com.albat.mobachir.util.DialogHelper;
import com.albat.mobachir.util.SharedPreferencesManager;

public class FixtureEventsFragment extends BaseFragment {
    String TAG = "FixtureEventsFragment";
    //Views
    private RecyclerView recyclerView;
    private Fixture fixture;

    //List
    private EventsAdapter adapter;
    private TextView noComments;

    boolean isInitialized = false;

    DialogHelper dialogHelper;
    SharedPreferencesManager sharedPreferencesManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_fixture_events, null);
        setHasOptionsMenu(true);

        dialogHelper = new DialogHelper();
        sharedPreferencesManager = SharedPreferencesManager.getInstance(getActivity());

        recyclerView = view.findViewById(R.id.list);
        noComments = view.findViewById(R.id.noComments);

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new EventsAdapter(getContext());
        recyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), mLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.list_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);

        isInitialized = true;
        if (fixture != null)
            loadItems();

        return view;
    }

    public void loadItems() {
        if (fixture.lineupData == null || (fixture.lineupData.team1.isEmpty() && fixture.lineupData.team2.isEmpty())) {
            noComments.setVisibility(View.VISIBLE);
            return;
        }
        noComments.setVisibility(View.GONE);
        adapter.setEvents(fixture.events);
        adapter.notifyDataSetChanged();
    }


    public void setFixture(Fixture fixture) {
        this.fixture = fixture;
        if (isInitialized)
            loadItems();
    }
}
