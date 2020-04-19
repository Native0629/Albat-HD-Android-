package com.albat.mobachir.providers.news;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.albat.mobachir.Config;
import com.albat.mobachir.R;
import com.albat.mobachir.network.models.News;
import com.albat.mobachir.util.CalendarHelper;
import com.albat.mobachir.util.InfiniteRecyclerViewAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends InfiniteRecyclerViewAdapter {
    private static final int LIST_AD_DELTA = 6;
    private static int AD_TYPE = 0;
    private static int NEWS_TYPE = 1;
    private ArrayList<News> data;
    private Context context;
    private OnOverViewClick callback;

    public NewsAdapter(ArrayList<News> data, Context context, OnOverViewClick click) {
        super(context, null);
        this.data = data;
        this.context = context;
        this.callback = click;
    }

    @Override
    protected int getViewType(int position) {
        if (position > 0 && position % LIST_AD_DELTA == 0) {
            return AD_TYPE;
        }
        return NEWS_TYPE;
    }


    @Override
    protected RecyclerView.ViewHolder getViewHolder(ViewGroup parent, int viewType) {
        if (viewType == AD_TYPE) {
            String adId = context.getResources().getString(R.string.admob_banner_id);

            AdView adView = new AdView(context);
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(adId);
            float density = context.getResources().getDisplayMetrics().density;
            int height = Math.round(AdSize.BANNER.getHeight() * density);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, height);
            adView.setLayoutParams(params);

            AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
            adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
            adView.loadAd(adRequestBuilder.build());
            return new AddViewHolder(adView);
        }

        return new CViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_news_item, parent, false));
    }

    @Override
    protected void doBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AddViewHolder)
            return;

        final int realPosition = getRealPosition(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onOverViewSelected(data.get(realPosition));
            }
        });

        News item = data.get(realPosition);
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

    private int getRealPosition(int position) {
        if (LIST_AD_DELTA == 0) {
            return position;
        } else {
            return position - position / LIST_AD_DELTA;
        }
    }

    @Override
    protected int getCount() {
        int additionalContent = 0;
        if (data.size() > 0 && LIST_AD_DELTA > 0 && data.size() > LIST_AD_DELTA) {
            additionalContent = data.size() / LIST_AD_DELTA;
        }
        return data.size() + additionalContent;
    }

    private class AddViewHolder extends RecyclerView.ViewHolder {

        public View itemView;

        private AddViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

        }
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