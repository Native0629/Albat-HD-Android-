package com.albat.mobachir.providers.leagues;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.albat.mobachir.Config;
import com.albat.mobachir.R;
import com.albat.mobachir.network.models.Guess;
import com.albat.mobachir.util.CalendarHelper;
import com.albat.mobachir.util.InfiniteRecyclerViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GuessAdapter extends InfiniteRecyclerViewAdapter {
    private ArrayList<Guess> data;
    private Context context;

    public GuessAdapter(ArrayList<Guess> data, Context context) {
        super(context, null);
        this.data = data;
        this.context = context;
    }


    @Override
    protected int getViewType(int position) {
        return 0;
    }

    @Override
    protected RecyclerView.ViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        return new CViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_guess_item, parent, false));
    }

    @Override
    protected void doBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Guess item = data.get(position);
        CViewHolder viewHolder = (CViewHolder) holder;
        viewHolder.team1.setText(item.fixture.team1);
        viewHolder.team2.setText(item.fixture.team2);
        viewHolder.dateTime.setText(CalendarHelper.reformatDateString(item.fixture.dateTime, Config.API_DATETIME_FORMAT, Config.APP_DATETIME_FORMAT, item.fixture.timezone));

        try {
            Picasso.with(context).load(item.fixture.team1Picture).error(R.drawable.ic_team_no_image).placeholder(R.drawable.ic_team_no_image).into(viewHolder.team1Picture);
        } catch (Exception e) {
            viewHolder.team1Picture.setImageResource(R.drawable.ic_team_no_image);
        }

        try {
            Picasso.with(context).load(item.fixture.team2Picture).error(R.drawable.ic_team_no_image).placeholder(R.drawable.ic_team_no_image).into(viewHolder.team2Picture);
        } catch (Exception e) {
            viewHolder.team2Picture.setImageResource(R.drawable.ic_team_no_image);
        }

        if (item.winnerBid != null) {
            viewHolder.winnerGuessLayout.setVisibility(View.VISIBLE);
            switch (item.fixtureStatus) {
                case 0:
                    viewHolder.fixtureGuess.setText("تعادل");
                    break;
                case 1:
                    viewHolder.fixtureGuess.setText("فوز " + item.fixture.team1);
                    break;
                case 2:
                    viewHolder.fixtureGuess.setText("فوز " + item.fixture.team2);
                    break;
            }
        } else
            viewHolder.winnerGuessLayout.setVisibility(View.GONE);

        if (item.resolved) {
            viewHolder.receivedPoints.setText(item.pointsReceived + "");
            viewHolder.goalsResult.setText(item.fixture.team1Goals + " - " + item.fixture.team2Goals);
            if (item.fixture.team1Goals == item.fixture.team2Goals)
                viewHolder.fixtureResult.setText("تعادل");
            else if (item.fixture.team1Goals > item.fixture.team2Goals)
                viewHolder.fixtureResult.setText("فوز " + item.fixture.team1);
            else if (item.fixture.team1Goals < item.fixture.team2Goals)
                viewHolder.fixtureResult.setText("فوز " + item.fixture.team2);
        } else {
            viewHolder.receivedPoints.setText("في انتظار النتيجة");
            viewHolder.goalsResult.setText("~");
            viewHolder.fixtureResult.setText("~");
        }

        if (item.team1Goals != null && item.team2Goals != null) {
            viewHolder.goalsGuessLayout.setVisibility(View.VISIBLE);
            viewHolder.goalsGuess.setText(item.team1Goals + " - " + item.team2Goals);

        } else
            viewHolder.goalsGuessLayout.setVisibility(View.GONE);


    }

    @Override
    protected int getCount() {
        return data.size();
    }

    private class CViewHolder extends RecyclerView.ViewHolder {
        public TextView team1;
        public TextView team2;
        public TextView dateTime;
        public CircleImageView team1Picture;
        public CircleImageView team2Picture;


        public TextView league;
        public TextView fixtureGuess;
        public TextView goalsGuess;
        public TextView fixtureResult;
        public TextView goalsResult;
        public TextView receivedPoints;
        public LinearLayout goalsGuessLayout;
        public LinearLayout winnerGuessLayout;


        public View itemView;

        private CViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            team1 = itemView.findViewById(R.id.team1);
            team2 = itemView.findViewById(R.id.team2);
            dateTime = itemView.findViewById(R.id.dateTime);
            team1Picture = itemView.findViewById(R.id.team1Picture);
            team2Picture = itemView.findViewById(R.id.team2Picture);

            league = itemView.findViewById(R.id.league);
            fixtureGuess = itemView.findViewById(R.id.fixtureGuess);
            goalsGuess = itemView.findViewById(R.id.goalsGuess);
            fixtureResult = itemView.findViewById(R.id.fixtureResult);
            goalsResult = itemView.findViewById(R.id.goalsResult);
            receivedPoints = itemView.findViewById(R.id.receivedPoints);
            goalsGuessLayout = itemView.findViewById(R.id.goalsGuessLayout);
            winnerGuessLayout = itemView.findViewById(R.id.winnerGuessLayout);
        }
    }

    public void clear() {
        data.clear();
    }
}