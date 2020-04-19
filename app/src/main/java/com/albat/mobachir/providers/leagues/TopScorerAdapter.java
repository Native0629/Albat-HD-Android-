package com.albat.mobachir.providers.leagues;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.albat.mobachir.R;
import com.albat.mobachir.network.models.TopScorer;
import com.albat.mobachir.util.InfiniteRecyclerViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TopScorerAdapter extends InfiniteRecyclerViewAdapter {
    private ArrayList<TopScorer> data;
    private Context context;

    public TopScorerAdapter(ArrayList<TopScorer> data, Context context) {
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
        return new CViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_topscorer_item, parent, false));
    }

    @Override
    protected void doBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        TopScorer item = data.get(position);
        CViewHolder viewHolder = (CViewHolder) holder;
        viewHolder.rank.setText(item.rank + "");
        viewHolder.playerName.setText(item.arName);
        viewHolder.teamName.setText(item.team);
        viewHolder.goals.setText(item.goals + "");

        try {
            Picasso.with(context).load(item.playerPicture).error(R.drawable.no_image).placeholder(R.drawable.no_image).into(viewHolder.picture);
        } catch (Exception e) {
            viewHolder.picture.setImageResource(R.drawable.no_image);
        }
    }

    @Override
    protected int getCount() {
        return data.size();
    }

    private class CViewHolder extends RecyclerView.ViewHolder {
        public TextView rank;
        public TextView playerName;
        public TextView teamName;
        public TextView goals;
        public CircleImageView picture;

        public View itemView;

        private CViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            rank = itemView.findViewById(R.id.rank);
            playerName = itemView.findViewById(R.id.playerName);
            teamName = itemView.findViewById(R.id.teamName);
            goals = itemView.findViewById(R.id.goals);
            picture = itemView.findViewById(R.id.picture);
        }
    }

    public void clear() {
        data.clear();
    }
}