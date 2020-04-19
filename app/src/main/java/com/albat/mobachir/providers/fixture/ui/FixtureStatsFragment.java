package com.albat.mobachir.providers.fixture.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.albat.mobachir.BaseFragment;
import com.albat.mobachir.R;
import com.albat.mobachir.network.models.Fixture;
import com.albat.mobachir.network.models.Stats;

public class FixtureStatsFragment extends BaseFragment {
    String TAG = "FixtureStatsFragment";
    //Views
    private Fixture fixture;

    boolean isInitialized = false;

    private TextView noComments;

    RoundCornerProgressBar t1PossessionBar, t1OnGoalShotsBar, t1OffsidesBar, t1FoulsBar, t1CornersBar, t1freeKicksBar, t1YellowCardsBar, t1RedCardsBar, t1SubstitutionsBar;
    TextView t1Possession, t1OnGoalShots, t1Offsides, t1Fouls, t1Corners, t1freeKicks, t1YellowCards, t1RedCards, t1Substitutions;

    RoundCornerProgressBar t2PossessionBar, t2OnGoalShotsBar, t2OffsidesBar, t2FoulsBar, t2CornersBar, t2freeKicksBar, t2YellowCardsBar, t2RedCardsBar, t2SubstitutionsBar;
    TextView t2Possession, t2OnGoalShots, t2Offsides, t2Fouls, t2Corners, t2freeKicks, t2YellowCards, t2RedCards, t2Substitutions;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_fixture_stats, null);
        setHasOptionsMenu(true);

        noComments = view.findViewById(R.id.noComments);

        t1PossessionBar = view.findViewById(R.id.t1PossessionBar);
        t1OnGoalShotsBar = view.findViewById(R.id.t1OnGoalShotsBar);
        t1OffsidesBar = view.findViewById(R.id.t1OffsidesBar);
        t1FoulsBar = view.findViewById(R.id.t1FoulsBar);
        t1CornersBar = view.findViewById(R.id.t1CornersBar);
        t1freeKicksBar = view.findViewById(R.id.t1freeKicksBar);
        t1YellowCardsBar = view.findViewById(R.id.t1YellowCardsBar);
        t1RedCardsBar = view.findViewById(R.id.t1RedCardsBar);
        t1SubstitutionsBar = view.findViewById(R.id.t1SubstitutionsBar);

        t1Possession = view.findViewById(R.id.t1Possession);
        t1OnGoalShots = view.findViewById(R.id.t1OnGoalShots);
        t1Offsides = view.findViewById(R.id.t1Offsides);
        t1Fouls = view.findViewById(R.id.t1Fouls);
        t1Corners = view.findViewById(R.id.t1Corners);
        t1freeKicks = view.findViewById(R.id.t1freeKicks);
        t1YellowCards = view.findViewById(R.id.t1YellowCards);
        t1RedCards = view.findViewById(R.id.t1RedCards);
        t1Substitutions = view.findViewById(R.id.t1Substitutions);

        t2PossessionBar = view.findViewById(R.id.t2PossessionBar);
        t2OnGoalShotsBar = view.findViewById(R.id.t2OnGoalShotsBar);
        t2OffsidesBar = view.findViewById(R.id.t2OffsidesBar);
        t2FoulsBar = view.findViewById(R.id.t2FoulsBar);
        t2CornersBar = view.findViewById(R.id.t2CornersBar);
        t2freeKicksBar = view.findViewById(R.id.t2freeKicksBar);
        t2YellowCardsBar = view.findViewById(R.id.t2YellowCardsBar);
        t2RedCardsBar = view.findViewById(R.id.t2RedCardsBar);
        t2SubstitutionsBar = view.findViewById(R.id.t2SubstitutionsBar);

        t2Possession = view.findViewById(R.id.t2Possession);
        t2OnGoalShots = view.findViewById(R.id.t2OnGoalShots);
        t2Offsides = view.findViewById(R.id.t2Offsides);
        t2Fouls = view.findViewById(R.id.t2Fouls);
        t2Corners = view.findViewById(R.id.t2Corners);
        t2freeKicks = view.findViewById(R.id.t2freeKicks);
        t2YellowCards = view.findViewById(R.id.t2YellowCards);
        t2RedCards = view.findViewById(R.id.t2RedCards);
        t2Substitutions = view.findViewById(R.id.t2Substitutions);

        isInitialized = true;
        if (fixture != null)
            loadItems();

        return view;
    }

    public void loadItems() {
        if (fixture.stats == null) {
            noComments.setVisibility(View.VISIBLE);
            return;
        }

        noComments.setVisibility(View.GONE);

        Stats stats = fixture.stats;
        setPercentage(stats.t1Possession, stats.t2Possession, t1PossessionBar, t1Possession, t2PossessionBar, t2Possession);
        setValue(stats.t1OnGoalShots, stats.t2OnGoalShots, t1OnGoalShotsBar, t1OnGoalShots, t2OnGoalShotsBar, t2OnGoalShots);
        setValue(stats.t1Offsides, stats.t2Offsides, t1OffsidesBar, t1Offsides, t2OffsidesBar, t2Offsides);
        setValue(stats.t1Fouls, stats.t2Fouls, t1FoulsBar, t1Fouls, t2FoulsBar, t2Fouls);
        setValue(stats.t1Corners, stats.t2Corners, t1CornersBar, t1Corners, t2CornersBar, t2Corners);
        setValue(stats.t1FreeKicks, stats.t2FreeKicks, t1freeKicksBar, t1freeKicks, t2freeKicksBar, t2freeKicks);
        setValue(stats.t1YellowCards, stats.t2YellowCards, t1YellowCardsBar, t1YellowCards, t2YellowCardsBar, t2YellowCards);
        setValue(stats.t1RedCards, stats.t2RedCards, t1RedCardsBar, t1RedCards, t2RedCardsBar, t2RedCards);
        setValue(stats.t1Substitutions, stats.t2Substitutions, t1SubstitutionsBar, t1Substitutions, t2SubstitutionsBar, t2Substitutions);
    }

    private void setValue(int v1, int v2, RoundCornerProgressBar p1, TextView t1, RoundCornerProgressBar p2, TextView t2) {
        t1.setText(v1 + "");
        t2.setText(v2 + "");

        p1.setMax(v1 + v2);
        p2.setMax(v1 + v2);

        p1.setProgress(v1);
        p2.setProgress(v2);
    }

    private void setPercentage(int v1, int v2, RoundCornerProgressBar p1, TextView t1, RoundCornerProgressBar p2, TextView t2) {
        t1.setText(v1 + "%");
        t2.setText(v2 + "%");

        p1.setMax(v1 + v2);
        p2.setMax(v1 + v2);

        p1.setProgress(v1);
        p2.setProgress(v2);
    }


    public void setFixture(Fixture fixture) {
        this.fixture = fixture;
        if (isInitialized)
            loadItems();
    }
}
