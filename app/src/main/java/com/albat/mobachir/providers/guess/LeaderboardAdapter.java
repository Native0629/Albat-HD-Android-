package com.albat.mobachir.providers.guess;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.albat.mobachir.App;
import com.albat.mobachir.R;
import com.albat.mobachir.network.models.Leaderboard;
import com.albat.mobachir.util.InfiniteRecyclerViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class LeaderboardAdapter extends InfiniteRecyclerViewAdapter {
    private static final String TAG = "LeaderboardAdapter";
    private ArrayList<Leaderboard> data;
    private Context context;

    public LeaderboardAdapter(ArrayList<Leaderboard> data, Context context) {
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
        return new CViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_leaderboard_item, parent, false));
    }

    @Override
    protected void doBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Leaderboard item = data.get(position);
        CViewHolder viewHolder = (CViewHolder) holder;
        viewHolder.rank.setText(item.rank + "");
        viewHolder.name.setText(item.name);
        viewHolder.points.setText(item.points + "");

        if (item.rank == 1) {
            viewHolder.picture.setImageResource(R.drawable.ic_number_one);
        } else {
            try {
                Picasso.with(context).load(item.picture).error(R.drawable.no_image).placeholder(R.drawable.no_image).into(viewHolder.picture);
            } catch (Exception e) {
                viewHolder.picture.setImageResource(R.drawable.no_image);
            }
        }


        if (App.getInstance().getUser().id == item.id) {
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#F0C075"));
        } else {
            if (position < 3) {
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#DBEBD8"));
            } else
                viewHolder.itemView.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    protected int getCount() {
        return data.size();
    }

    private class CViewHolder extends RecyclerView.ViewHolder {
        public TextView rank;
        public TextView name;
        public TextView points;
        public CircleImageView picture;

        public View itemView;

        private CViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            rank = itemView.findViewById(R.id.rank);
            name = itemView.findViewById(R.id.name);
            points = itemView.findViewById(R.id.points);
            picture = itemView.findViewById(R.id.picture);
        }
    }

    public void clear() {
        data.clear();
    }
}