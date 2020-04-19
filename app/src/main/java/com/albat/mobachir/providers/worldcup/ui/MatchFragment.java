package com.albat.mobachir.providers.worldcup.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.albat.mobachir.Config;
import com.albat.mobachir.FullScreenActivity;
import com.albat.mobachir.MainActivity;
import com.albat.mobachir.R;
import com.albat.mobachir.inherit.BackPressFragment;
import com.albat.mobachir.providers.worldcup.LinkItem;
import com.albat.mobachir.providers.worldcup.LinksAdapter;
import com.albat.mobachir.providers.worldcup.MatchItem;
import com.albat.mobachir.util.CalendarHelper;
import com.albat.mobachir.util.Log;
import com.squareup.picasso.Picasso;

public class MatchFragment extends Fragment implements BackPressFragment, LinksAdapter.OnOverViewClick {
    String TAG = "MatchFragment";
    //Views
    public TextView title;
    public TextView commentator;
    public TextView channel;
    public TextView team1;
    public ImageView team1Flag;
    public TextView team2;
    public ImageView team2Flag;
    public TextView time;
    public TextView stadium;
    public TextView notes;

    private RelativeLayout rl;
    private MatchItem matchItem;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("matchItem", matchItem);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rl = (RelativeLayout) inflater.inflate(R.layout.fragment_worldcup_match, null);
        setHasOptionsMenu(true);
        rl.setBackgroundColor(Color.parseColor("#F9F9F9"));

        title = rl.findViewById(R.id.title);
        commentator = rl.findViewById(R.id.commentator);
        channel = rl.findViewById(R.id.channel);
        stadium = rl.findViewById(R.id.stadium);
        team1 = rl.findViewById(R.id.team1Name);
        team1Flag = rl.findViewById(R.id.team1Flag);
        team2 = rl.findViewById(R.id.team2Name);
        team2Flag = rl.findViewById(R.id.team2Flag);
        time = rl.findViewById(R.id.time);
        notes = rl.findViewById(R.id.notes);

        if (savedInstanceState != null)
            matchItem = (MatchItem) savedInstanceState.getSerializable("matchItem");
        if (matchItem == null)
            matchItem = new MatchItem();

        title.setText(matchItem.getTitle());
        team1.setText(matchItem.getTeam1().getName());
        team2.setText(matchItem.getTeam2().getName());
        time.setText(CalendarHelper.reformatDateString(matchItem.getDateTime(), Config.API_DATETIME_FORMAT, "hh:mm a", matchItem.getTimezone()));
        commentator.setText(matchItem.getCommentator());
        channel.setText(matchItem.getChannel());
        stadium.setText(matchItem.getStadium());

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
                Log.e(TAG, e.getMessage(), e);
                team2Flag.setImageResource(R.drawable.flag_unknown);
            }
        }

        if (matchItem.getNotes() == null || matchItem.getNotes().isEmpty())
            notes.setVisibility(View.GONE);
        else {
            notes.setVisibility(View.VISIBLE);
            notes.setText(matchItem.getNotes());
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
        return rl;
    }

    @Override
    public void onOverViewSelected(LinkItem link) {
        Intent intent = new Intent(getActivity(), FullScreenActivity.class);
        intent.putExtra(FullScreenActivity.LINK, link.getLink());
        startActivity(intent);
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

    public MatchFragment setMatchItem(MatchItem matchItem) {
        this.matchItem = matchItem;
        return this;
    }
}
