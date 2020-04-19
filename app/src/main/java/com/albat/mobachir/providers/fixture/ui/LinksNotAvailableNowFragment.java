package com.albat.mobachir.providers.fixture.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.albat.mobachir.BaseFragment;
import com.albat.mobachir.Config;
import com.albat.mobachir.FullScreenActivity;
import com.albat.mobachir.MainActivity;
import com.albat.mobachir.R;
import com.albat.mobachir.network.models.Fixture;
import com.albat.mobachir.providers.worldcup.LinkItem;
import com.albat.mobachir.providers.worldcup.LinksAdapter;
import com.albat.mobachir.util.CalendarHelper;
import com.albat.mobachir.util.Log;
import com.squareup.picasso.Picasso;

public class LinksNotAvailableNowFragment extends BaseFragment implements LinksAdapter.OnOverViewClick {
    String TAG = "LinksNotAvailableNowFragment";
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
    private Fixture fixture;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("fixture", fixture);
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
            fixture = (Fixture) savedInstanceState.getSerializable("fixture");
        if (fixture == null)
            fixture = new Fixture();

        title.setText(fixture.league != null ? fixture.league.arName : fixture.title);
        team1.setText(fixture.team1);
        team2.setText(fixture.team2);
        time.setText(CalendarHelper.reformatDateString(fixture.dateTime, Config.API_DATETIME_FORMAT, "hh:mm a", fixture.timezone));
        commentator.setText(fixture.commentator);
        channel.setText(fixture.channel);
        stadium.setText(fixture.stadium);

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

        notes.setText("قنوات البث قبل بداية المباراة");

        rl.findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).showInterstitial();
                }

                goBack();
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

    public LinksNotAvailableNowFragment setFixture(Fixture fixture) {
        this.fixture = fixture;
        return this;
    }
}
