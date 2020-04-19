package com.albat.mobachir.providers.worldcup;

import android.content.Context;
import android.content.res.Resources;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.albat.mobachir.Config;
import com.albat.mobachir.R;
import com.albat.mobachir.util.CalendarHelper;
import com.albat.mobachir.util.Helper;
import com.albat.mobachir.util.InfiniteRecyclerViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MatchesAdapter extends InfiniteRecyclerViewAdapter {
    private List<MatchItem> data;
    private Context context;
    private OnOverViewClick callback;

    private int number;

    public MatchesAdapter(List<MatchItem> data, Context context, OnOverViewClick click) {
        super(context, null);
        this.data = data;
        this.context = context;
        this.callback = click;
    }


    @Override
    protected int getViewType(int position) {
        return 0;
    }

    @Override
    protected RecyclerView.ViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        return new MatchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_worldcup_item, parent, false));
    }

    @Override
    protected void doBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onOverViewSelected(data.get(holder.getAdapterPosition()));
            }
        });

        MatchItem matchItem = data.get(position);
        MatchViewHolder matchViewHolder = (MatchViewHolder) holder;
        matchViewHolder.title.setText(matchItem.getTitle());
        matchViewHolder.team1.setText(matchItem.getTeam1().getName());
        matchViewHolder.team2.setText(matchItem.getTeam2().getName());
        matchViewHolder.commentator.setText(matchItem.getCommentator());
        matchViewHolder.channel.setText(matchItem.getChannel());
        matchViewHolder.time.setText(CalendarHelper.reformatDateString(matchItem.getInfo(), Config.API_DATETIME_FORMAT, "hh:mm a", matchItem.timezone));

        Resources resources = context.getResources();
        if (matchItem.getTeam1().getCode() != null && !matchItem.getTeam1().getCode().isEmpty()) {
            int team1FlagResource = resources.getIdentifier("flag_" + matchItem.getTeam1().getCode().toLowerCase(), "drawable", context.getPackageName());
            matchViewHolder.team1Flag.setImageResource(team1FlagResource != 0 ? team1FlagResource : R.drawable.flag_unknown);
        } else {
            try {
                Picasso.with(context).load(matchItem.getTeam1().getFlag()).error(R.drawable.flag_unknown).placeholder(R.drawable.flag_unknown).into(matchViewHolder.team1Flag);
            } catch (Exception e) {
                matchViewHolder.team1Flag.setImageResource(R.drawable.flag_unknown);
            }
        }
        if (matchItem.getTeam2().getCode() != null && !matchItem.getTeam2().getCode().isEmpty()) {
            int team2FlagResource = resources.getIdentifier("flag_" + matchItem.getTeam2().getCode().toLowerCase(), "drawable", context.getPackageName());
            matchViewHolder.team2Flag.setImageResource(team2FlagResource != 0 ? team2FlagResource : R.drawable.flag_unknown);
        } else {
            try {
                Picasso.with(context).load(matchItem.getTeam2().getFlag()).error(R.drawable.flag_unknown).placeholder(R.drawable.flag_unknown).into(matchViewHolder.team2Flag);
            } catch (Exception e) {
                matchViewHolder.team2Flag.setImageResource(R.drawable.flag_unknown);
            }
        }
    }

    @Override
    protected int getCount() {
        return data.size();
    }

    private class MatchViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView team1;
        public ImageView team1Flag;
        public TextView team2;
        public ImageView team2Flag;
        public TextView time;
        public TextView commentator;
        public TextView channel;
        public View itemView;

        private MatchViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            title = itemView.findViewById(R.id.title);
            team1 = itemView.findViewById(R.id.team1Name);
            team1Flag = itemView.findViewById(R.id.team1Flag);
            team2 = itemView.findViewById(R.id.team2Name);
            team2Flag = itemView.findViewById(R.id.team2Flag);
            time = itemView.findViewById(R.id.time);
            commentator = itemView.findViewById(R.id.commentator);
            channel = itemView.findViewById(R.id.channel);
        }
    }

    private int randomGradientResource() {
        number += 1;
        if (number == 6) number = 1;

        return Helper.getGradient(number);
    }

    public interface OnOverViewClick {
        void onOverViewSelected(MatchItem item);
    }

    public void clear() {
        data.clear();
    }
}