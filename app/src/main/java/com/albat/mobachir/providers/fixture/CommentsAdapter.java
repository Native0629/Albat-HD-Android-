package com.albat.mobachir.providers.fixture;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.albat.mobachir.Config;
import com.albat.mobachir.R;
import com.albat.mobachir.network.models.Comment;
import com.albat.mobachir.util.CalendarHelper;
import com.albat.mobachir.util.InfiniteRecyclerViewAdapter;

import java.util.ArrayList;

public class CommentsAdapter extends InfiniteRecyclerViewAdapter {
    private ArrayList<Comment> data;
    private Context context;

    public CommentsAdapter(ArrayList<Comment> data, Context context) {
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
        return new CViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_comment_item, parent, false));
    }

    @Override
    protected void doBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Comment item = data.get(position);
        CViewHolder viewHolder = (CViewHolder) holder;
        viewHolder.comment.setText(item.content);
        viewHolder.username.setText(item.author);
        viewHolder.pubDate.setText(CalendarHelper.reformatDateString(item.pubDate, Config.API_DATETIME_FORMAT, Config.APP_DATETIME_FORMAT));

        try {
            //Picasso.with(context).load(item.picture).error(R.drawable.news_placeholder).placeholder(R.drawable.news_placeholder).into(viewHolder.picture);
        } catch (Exception e) {
            viewHolder.picture.setImageResource(R.drawable.news_placeholder);
        }
    }

    @Override
    protected int getCount() {
        return data.size();
    }

    private class CViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public TextView comment;
        public TextView pubDate;
        public ImageView picture;

        public View itemView;

        private CViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            username = itemView.findViewById(R.id.username);
            comment = itemView.findViewById(R.id.comment);
            pubDate = itemView.findViewById(R.id.pubDate);
            picture = itemView.findViewById(R.id.picture);
        }
    }


    public void clear() {
        data.clear();
    }
}