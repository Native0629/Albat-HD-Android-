package com.albat.mobachir.providers.fixture;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.albat.mobachir.R;
import com.albat.mobachir.network.models.FixtureLink;
import com.albat.mobachir.util.Helper;
import com.albat.mobachir.util.InfiniteRecyclerViewAdapter;

import java.util.List;

public class LinksAdapter extends InfiniteRecyclerViewAdapter {
    private List<FixtureLink> data;
    private Context context;
    private OnOverViewClick callback;

    private int number;

    public LinksAdapter(List<FixtureLink> data, Context context, OnOverViewClick click) {
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
        return new MatchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_worldcup_link, parent, false));
    }

    @Override
    protected void doBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onOverViewSelected(data.get(position));
            }
        });

        FixtureLink link = data.get(position);
        MatchViewHolder matchViewHolder = (MatchViewHolder) holder;
        matchViewHolder.channelName.setText(link.linkName);
    }

    @Override
    protected int getCount() {
        return data.size();
    }

    private class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;

        public View itemView;

        private ImageViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image);
        }
    }

    private class MatchViewHolder extends RecyclerView.ViewHolder {
        public TextView channelName;
        public View itemView;

        private MatchViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            channelName = itemView.findViewById(R.id.channelName);
        }
    }

    private int randomGradientResource() {
        number += 1;
        if (number == 6) number = 1;

        return Helper.getGradient(number);
    }

    public interface OnOverViewClick {
        void onOverViewSelected(FixtureLink link);
    }

    public void addAll(List<FixtureLink> items) {
        data.addAll(items);
    }

    public void clear() {
        data.clear();
    }
}