package com.albat.mobachir.providers.worldcup.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
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
import android.widget.Toast;

import com.albat.mobachir.Config;
import com.albat.mobachir.FullScreenActivity;
import com.albat.mobachir.MainActivity;
import com.albat.mobachir.R;
import com.albat.mobachir.VideoPlayerActivity;
import com.albat.mobachir.inherit.BackPressFragment;
import com.albat.mobachir.network.models.MessageEvent;
import com.albat.mobachir.providers.worldcup.LinkItem;
import com.albat.mobachir.providers.worldcup.LinksAdapter;
import com.albat.mobachir.providers.worldcup.MatchItem;
import com.albat.mobachir.providers.worldcup.MatchParser;
import com.albat.mobachir.util.CalendarHelper;
import com.albat.mobachir.util.Helper;
import com.albat.mobachir.util.InfiniteRecyclerViewAdapter;
import com.albat.mobachir.util.Log;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LinksFragment extends Fragment implements BackPressFragment, LinksAdapter.OnOverViewClick {
    String TAG = "LinksFragment";
    //Views
    public TextView team1;
    public ImageView team1Flag;
    public TextView team2;
    public ImageView team2Flag;
    public TextView time;

    private RelativeLayout rl;
    private RecyclerView mRecyclerView;
    private FloatingActionButton refreshFab;
    private MatchItem matchItem;

    private DividerItemDecoration horizontalDec;

    private String matchesUrl;
    //List
    private LinksAdapter linksAdapter;

    private ViewTreeObserver.OnGlobalLayoutListener recyclerListener;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("matchItem", matchItem);
        outState.putString("matchesUrl", matchesUrl);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rl = (RelativeLayout) inflater.inflate(R.layout.fragment_worldcup_links, null);
        setHasOptionsMenu(true);
        rl.setBackgroundColor(Color.parseColor("#F9F9F9"));
        mRecyclerView = rl.findViewById(R.id.list);


        team1 = rl.findViewById(R.id.team1Name);
        team1Flag = rl.findViewById(R.id.team1Flag);
        team2 = rl.findViewById(R.id.team2Name);
        team2Flag = rl.findViewById(R.id.team2Flag);
        time = rl.findViewById(R.id.time);

        if (savedInstanceState != null) {
            matchItem = (MatchItem) savedInstanceState.getSerializable("matchItem");
            matchesUrl = savedInstanceState.getString("matchesUrl");
        }
        if (matchItem == null)
            matchItem = new MatchItem();
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(true);
        linksAdapter = new LinksAdapter(matchItem.getLinks(), getContext(), LinksFragment.this);
        mRecyclerView.setAdapter(linksAdapter);
        linksAdapter.setModeAndNotify(matchItem.getLinks().isEmpty() ? InfiniteRecyclerViewAdapter.MODE_EMPTY : InfiniteRecyclerViewAdapter.MODE_LIST);

        horizontalDec = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.HORIZONTAL);

        mRecyclerView.addItemDecoration(horizontalDec);

        team1.setText(matchItem.getTeam1().getName());
        team2.setText(matchItem.getTeam2().getName());
        time.setText(CalendarHelper.reformatDateString(matchItem.getDateTime(), Config.API_DATETIME_FORMAT, "hh:mm a", matchItem.getTimezone()));

        Resources resources = getActivity().getResources();
        if (matchItem.getTeam1().getCode() != null && !matchItem.getTeam1().getCode().isEmpty()) {
            int team1FlagResource = resources.getIdentifier("flag_" + matchItem.getTeam1().getCode().toLowerCase(), "drawable", getActivity().getPackageName());
            team1Flag.setImageResource(team1FlagResource != 0 ? team1FlagResource : R.drawable.flag_unknown);
        } else {
            try {
                Picasso.with(getActivity()).load(matchItem.getTeam1().getFlag()).error(R.drawable.flag_unknown).placeholder(R.drawable.flag_unknown).into(team1Flag);
            } catch (Exception e) {
                team1Flag.setImageResource(R.drawable.flag_unknown);
            }
        }
        if (matchItem.getTeam2().getCode() != null && !matchItem.getTeam2().getCode().isEmpty()) {
            int team2FlagResource = resources.getIdentifier("flag_" + matchItem.getTeam2().getCode().toLowerCase(), "drawable", getActivity().getPackageName());
            team2Flag.setImageResource(team2FlagResource != 0 ? team2FlagResource : R.drawable.flag_unknown);
        } else {
            try {
                Picasso.with(getActivity()).load(matchItem.getTeam2().getFlag()).error(R.drawable.flag_unknown).placeholder(R.drawable.flag_unknown).into(team2Flag);
            } catch (Exception e) {
                team2Flag.setImageResource(R.drawable.flag_unknown);
            }
        }

        rl.findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).showInterstitial();
                }

                getActivity().onBackPressed();
            }
        });

        refreshFab = rl.findViewById(R.id.refresh);
        refreshFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).showInterstitial();
                }

                refreshFab.setVisibility(View.GONE);
                loadItems();
            }
        });
        refreshFab.setVisibility(View.GONE);

        return rl;
    }

    public void loadItems() {
        linksAdapter.clear();
        linksAdapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_PROGRESS);
        Log.e(TAG, matchesUrl + "/" + matchItem.getId() + "/" + (matchItem.isManual() ? 1 : 0));
        new MatchParser(matchesUrl + "/" + matchItem.getId(), getActivity(), new MatchParser.CallBack() {
            @Override
            public void matchLoaded(MatchItem result, boolean failed) {
                if (failed) {
                    //If it failed; show an error if we're using a local file, or if we are online & using a remote overview.
                    if (matchesUrl == null || !matchesUrl.contains("http") || Helper.isOnlineShowDialog(getActivity())) {
                        Toast.makeText(getActivity(), R.string.invalid_configuration, Toast.LENGTH_LONG).show();
                        linksAdapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_EMPTY);
                    }
                } else {
                    //Add all the new posts to the list and notify the adapter
                    matchItem.setLinks(result.getLinks());
                    linksAdapter.addAll(matchItem.getLinks());
                    linksAdapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_LIST);
                }
                //refreshFab.setVisibility(View.VISIBLE);
            }
        }).execute();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(recyclerListener);
    }

    @Override
    public void onOverViewSelected(LinkItem link) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showInterstitial();
        }
        if (link.getLink().endsWith("m3u8")) {
            Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
            intent.putExtra(VideoPlayerActivity.LINK, link.getLink());
            startActivity(intent);
            //GiraffePlayer.play(getContext(), new VideoInfo(Uri.parse(link.getLink())));
        } else {
            Intent intent = new Intent(getActivity(), FullScreenActivity.class);
            intent.putExtra(FullScreenActivity.LINK, link.getLink());
            startActivity(intent);
        }
    }

    @Override
    public boolean handleBackPress() {
        if (getChildFragmentManager().getBackStackEntryCount() > 0) {
            getChildFragmentManager().popBackStack();
            return true;
        }

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showInterstitial();
        }

        return false;
    }

    public LinksFragment setMatchItem(MatchItem matchItem) {
        this.matchItem = matchItem;
        return this;
    }

    public LinksFragment setMatchesUrl(String matchesUrl) {
        this.matchesUrl = matchesUrl;
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
