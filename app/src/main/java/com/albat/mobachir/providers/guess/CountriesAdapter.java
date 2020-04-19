package com.albat.mobachir.providers.guess;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.albat.mobachir.Config;
import com.albat.mobachir.R;
import com.albat.mobachir.network.models.News;
import com.albat.mobachir.util.CalendarHelper;
import com.albat.mobachir.util.InfiniteRecyclerViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CountriesAdapter extends InfiniteRecyclerViewAdapter {
    private ArrayList<News> data;
    private Context context;
    private OnOverViewClick callback;

    public CountriesAdapter(ArrayList<News> data, Context context, OnOverViewClick click) {
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
        return new CViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_news_item, parent, false));
    }

    @Override
    protected void doBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onOverViewSelected(data.get(holder.getAdapterPosition()));
            }
        });

        News item = data.get(position);
        CViewHolder viewHolder = (CViewHolder) holder;
        viewHolder.title.setText(item.title);
        viewHolder.source.setText(item.source);
        viewHolder.commentsCount.setText(item.commentsCount + "");
        viewHolder.pubDate.setText(CalendarHelper.reformatDateString(item.pubDate, Config.API_DATETIME_FORMAT, Config.APP_DATETIME_FORMAT));

        try {
            Picasso.with(context).load(item.picture).error(R.drawable.news_placeholder).placeholder(R.drawable.news_placeholder).into(viewHolder.picture);
        } catch (Exception e) {
            viewHolder.picture.setImageResource(R.drawable.news_placeholder);
        }
    }

    @Override
    protected int getCount() {
        return data.size();
    }

    private class CViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView content;
        public TextView source;
        public TextView commentsCount;
        public TextView pubDate;
        public ImageView picture;

        public View itemView;

        private CViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            title = itemView.findViewById(R.id.title);
            source = itemView.findViewById(R.id.source);
            commentsCount = itemView.findViewById(R.id.commentsCount);
            pubDate = itemView.findViewById(R.id.pubDate);
            picture = itemView.findViewById(R.id.picture);
        }
    }

    public interface OnOverViewClick {
        void onOverViewSelected(News item);
    }

    public void clear() {
        data.clear();
    }
}