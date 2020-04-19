package com.albat.mobachir.providers.fixture;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.albat.mobachir.R;
import com.albat.mobachir.network.models.Lineup;
import com.albat.mobachir.network.models.LineupData;
import com.albat.mobachir.util.InfiniteRecyclerViewAdapter;

public class LineupAdapter extends InfiniteRecyclerViewAdapter {
    private LineupData lineupData;
    private Context context;

    public LineupAdapter(Context context) {
        super(context, null);
        this.context = context;
    }


    @Override
    protected int getViewType(int position) {
        return 0;
    }

    @Override
    protected RecyclerView.ViewHolder getViewHolder(ViewGroup parent, int viewType) {
        return new CViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_fixture_lineup_item, parent, false));
    }

    @Override
    protected void doBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Lineup team1Player = lineupData.team1 != null && lineupData.team1.size() > position ? lineupData.team1.get(position) : null;
        Lineup team2Player = lineupData.team2 != null && lineupData.team2.size() > position ? lineupData.team2.get(position) : null;

        CViewHolder viewHolder = (CViewHolder) holder;
        viewHolder.team1PlayerName.setText(team1Player != null ? team1Player.playerName + " ." + (position + 1) : "");
        viewHolder.team2PlayerName.setText(team2Player != null ? team2Player.playerName + " ." + (position + 1) : "");
    }

    @Override
    protected int getCount() {
        if (lineupData == null)
            return 0;

        if (lineupData.team1 != null && lineupData.team2 != null)
            return lineupData.team1.size() > lineupData.team2.size() ? lineupData.team1.size() : lineupData.team2.size();
        if (lineupData.team1 != null)
            return lineupData.team1.size();
        if (lineupData.team2 != null)
            return lineupData.team2.size();

        return 0;
    }

    private class CViewHolder extends RecyclerView.ViewHolder {
        public TextView team1PlayerName;
        public TextView team2PlayerName;

        public View itemView;

        private CViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            team1PlayerName = itemView.findViewById(R.id.team1PlayerName);
            team2PlayerName = itemView.findViewById(R.id.team2PlayerName);
        }
    }

    public void setLineupData(LineupData lineupData) {
        this.lineupData = lineupData;
    }
}