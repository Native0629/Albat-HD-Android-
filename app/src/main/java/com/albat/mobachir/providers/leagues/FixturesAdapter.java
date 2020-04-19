package com.albat.mobachir.providers.leagues;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.albat.mobachir.Config;
import com.albat.mobachir.R;
import com.albat.mobachir.network.models.Fixture;
import com.albat.mobachir.network.models.Fixture.MatchStatus;
import com.albat.mobachir.util.CalendarHelper;
import com.albat.mobachir.util.InfiniteRecyclerViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FixturesAdapter extends InfiniteRecyclerViewAdapter {
    private ArrayList<Fixture> data;
    private Context context;

    public FixturesAdapter(ArrayList<Fixture> data, Context context) {
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
        return new CViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_fixtures_item, parent, false));
    }

    @Override
    protected void doBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Fixture item = data.get(position);
        CViewHolder viewHolder = (CViewHolder) holder;
        viewHolder.team1.setText(item.team1);
        viewHolder.team2.setText(item.team2);
        viewHolder.dateTime.setText(CalendarHelper.reformatDateString(item.dateTime, Config.API_DATETIME_FORMAT, "d/M hh:mma", item.timezone, false));

        viewHolder.vs.setText("Vs");
        viewHolder.team1Result.setVisibility(View.GONE);
        viewHolder.team2Result.setVisibility(View.GONE);
        switch (item.getStatus()) {
            case MatchStatus.NOT_STARTED:
            case MatchStatus.POSTPONED:
            case MatchStatus.DELAYED:
            case MatchStatus.CANCELED:
            case MatchStatus.SUSPENDED:
            case MatchStatus.INTERRUPTED:
            case MatchStatus.LIVE:
            case MatchStatus.HALF_TIME:
            case MatchStatus.BREAK:
            case MatchStatus.EXTRA_TIME:
            case MatchStatus.PENALITY_LIVE:
                break;
            case MatchStatus.ENDED_AFTER_FULL_TIME:
            case MatchStatus.ENDED_AFTER_EXTRA_TIME:
            case MatchStatus.ENDED_AFTER_PENALITY_SHOOTOUT:
            case MatchStatus.ENDED:
                if (!item.manual || (item.manual && item.resultAdded)) {
                    viewHolder.team1Result.setVisibility(View.VISIBLE);
                    viewHolder.team2Result.setVisibility(View.VISIBLE);

                    viewHolder.team1Result.setText(item.team1Goals + "");
                    viewHolder.team2Result.setText(item.team2Goals + "");
                    viewHolder.vs.setText(" - ");
                    if (item.team1PenGoals != 0 && item.team2PenGoals != 0) {
                        viewHolder.team2Result.setText(item.team2Goals + " (" + item.team2PenGoals + ")");
                        viewHolder.team1Result.setText("(" + item.team1PenGoals + ") " + item.team1Goals);
                    }
                }

                break;
        }


        try {
            Picasso.with(context).load(item.team1Picture).error(R.drawable.ic_team_no_image).placeholder(R.drawable.ic_team_no_image).into(viewHolder.team1Picture);
        } catch (Exception e) {
            viewHolder.team1Picture.setImageResource(R.drawable.ic_team_no_image);
        }

        try {
            Picasso.with(context).load(item.team2Picture).error(R.drawable.ic_team_no_image).placeholder(R.drawable.ic_team_no_image).into(viewHolder.team2Picture);
        } catch (Exception e) {
            viewHolder.team2Picture.setImageResource(R.drawable.ic_team_no_image);
        }
    }

    @Override
    protected int getCount() {
        return data.size();
    }

    private class CViewHolder extends RecyclerView.ViewHolder {
        public TextView team1;
        public TextView team2;
        public TextView team1Result;
        public TextView team2Result;
        public TextView dateTime;
        public TextView vs;
        public CircleImageView team1Picture;
        public CircleImageView team2Picture;

        public View itemView;

        private CViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            team1 = itemView.findViewById(R.id.team1);
            team2 = itemView.findViewById(R.id.team2);
            dateTime = itemView.findViewById(R.id.dateTime);
            team1Picture = itemView.findViewById(R.id.team1Picture);
            team2Picture = itemView.findViewById(R.id.team2Picture);
            vs = itemView.findViewById(R.id.vs);
            team1Result = itemView.findViewById(R.id.team1Result);
            team2Result = itemView.findViewById(R.id.team2Result);
        }
    }


    public void clear() {
        data.clear();
    }
}