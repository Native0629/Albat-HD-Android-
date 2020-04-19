package com.albat.mobachir.providers.fixture.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.albat.mobachir.BaseFragment;
import com.albat.mobachir.Config;
import com.albat.mobachir.FullScreenActivity;
import com.albat.mobachir.R;
import com.albat.mobachir.VideoPlayerActivity;
import com.albat.mobachir.network.interfaces.FixtureRetrieved;
import com.albat.mobachir.network.managers.FixturesManager;
import com.albat.mobachir.network.models.Fixture;
import com.albat.mobachir.network.models.FixtureLink;
import com.albat.mobachir.network.models.MessageEvent;
import com.albat.mobachir.providers.fixture.LinksAdapter;
import com.albat.mobachir.util.CalendarHelper;
import com.albat.mobachir.util.InfiniteRecyclerViewAdapter;
import com.albat.mobachir.util.Log;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ViewFixtureLinksFragment extends BaseFragment implements FixtureRetrieved, LinksAdapter.OnOverViewClick {
    String TAG = "ViewFixtureLinksFragment";
    //Views
    public TextView team1;
    public ImageView team1Flag;
    public TextView team2;
    public ImageView team2Flag;
    public TextView time;

    private RecyclerView mRecyclerView;
    private FloatingActionButton refreshFab;
    private Fixture fixture;

    private DividerItemDecoration horizontalDec;

    //List
    private LinksAdapter linksAdapter;

    private ViewTreeObserver.OnGlobalLayoutListener recyclerListener;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("fixture", fixture);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (RelativeLayout) inflater.inflate(R.layout.fragment_worldcup_links, null);
        setHasOptionsMenu(true);
        view.setBackgroundColor(Color.parseColor("#F9F9F9"));
        mRecyclerView = view.findViewById(R.id.list);


        team1 = view.findViewById(R.id.team1Name);
        team1Flag = view.findViewById(R.id.team1Flag);
        team2 = view.findViewById(R.id.team2Name);
        team2Flag = view.findViewById(R.id.team2Flag);
        time = view.findViewById(R.id.time);

        if (savedInstanceState != null) {
            fixture = (Fixture) savedInstanceState.getSerializable("fixture");
        }
        if (fixture == null)
            fixture = new Fixture();
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(true);

        linksAdapter = new LinksAdapter(fixture.links, getContext(), ViewFixtureLinksFragment.this);
        mRecyclerView.setAdapter(linksAdapter);
        linksAdapter.setModeAndNotify(fixture.links.isEmpty() ? InfiniteRecyclerViewAdapter.MODE_EMPTY : InfiniteRecyclerViewAdapter.MODE_LIST);

        horizontalDec = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.HORIZONTAL);

        mRecyclerView.addItemDecoration(horizontalDec);

        team1.setText(fixture.team1);
        team2.setText(fixture.team2);
        time.setText(CalendarHelper.reformatDateString(fixture.dateTime, Config.API_DATETIME_FORMAT, "hh:mm a", fixture.timezone));

        try {
            Picasso.with(getActivity()).load(fixture.team1Picture).error(R.drawable.flag_unknown).placeholder(R.drawable.flag_unknown).into(team1Flag);
        } catch (Exception e) {
            team1Flag.setImageResource(R.drawable.flag_unknown);
        }
        try {
            Picasso.with(getActivity()).load(fixture.team2Picture).error(R.drawable.flag_unknown).placeholder(R.drawable.flag_unknown).into(team2Flag);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            team2Flag.setImageResource(R.drawable.flag_unknown);
        }

        view.findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new MessageEvent(MessageEvent.SHOW_INTERSTITIAL_AD));

                goBack();
            }
        });

        refreshFab = view.findViewById(R.id.refresh);
        refreshFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new MessageEvent(MessageEvent.SHOW_INTERSTITIAL_AD));

                getFixture();
            }
        });
        refreshFab.setVisibility(View.GONE);

        return view;
    }

    private void getFixture() {
        refreshFab.setVisibility(View.GONE);
        linksAdapter.clear();
        linksAdapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_PROGRESS);

        FixturesManager fixturesManager = new FixturesManager();
        fixturesManager.getFixture(fixture.id, this);
    }

    @Override
    public void onFixtureRetrieved(Fixture fixture, boolean success, String error) {
        if (!success || fixture == null) {
            linksAdapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_EMPTY);
        } else {
            this.fixture = fixture;
            linksAdapter.clear();
            linksAdapter.addAll(fixture.links);
            linksAdapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_LIST);
        }
        //refreshFab.setVisibility(View.VISIBLE);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(recyclerListener);
    }

    @Override
    public void onOverViewSelected(FixtureLink link) {
        EventBus.getDefault().post(new MessageEvent(MessageEvent.SHOW_INTERSTITIAL_AD));

        if (link.link.endsWith("m3u8")) {
            Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
            intent.putExtra(VideoPlayerActivity.LINK, link.link);
            startActivity(intent);
            //GiraffePlayer.play(getContext(), new VideoInfo(Uri.parse(link.getLink())));
        } else {
            Intent intent = new Intent(getActivity(), FullScreenActivity.class);
            intent.putExtra(FullScreenActivity.LINK, link.link);
            startActivity(intent);
        }
    }


    public ViewFixtureLinksFragment setFixture(Fixture fixture) {
        this.fixture = fixture;
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
                getFixture();
                break;
        }
    }
}
