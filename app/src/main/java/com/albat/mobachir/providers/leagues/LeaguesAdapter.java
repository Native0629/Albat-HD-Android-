package com.albat.mobachir.providers.leagues;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.albat.mobachir.R;
import com.albat.mobachir.network.models.League;
import com.squareup.picasso.Picasso;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersBaseAdapter;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleArrayAdapter;

import java.util.List;

public class LeaguesAdapter extends BaseAdapter implements StickyGridHeadersBaseAdapter {

    private LayoutInflater layoutinflater;
    private List<League> list;
    private Context context;
    private int arabicCount = 0;
    private int worldCount;

    public LeaguesAdapter(Context context, List<League> list) {
        this.context = context;
        layoutinflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutinflater.inflate(R.layout.fragment_leagues_item, parent, false);
            viewHolder.name = convertView.findViewById(R.id.name);
            viewHolder.logo = convertView.findViewById(R.id.picture);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        League league = list.get(position);
        viewHolder.name.setText(league.arName);
//        try {
//            Picasso.with(context).load(league.picture).error(R.drawable.ic_league).placeholder(R.drawable.ic_league).into(viewHolder.logo);
//        } catch (Exception e) {
//            viewHolder.logo.setImageResource(R.drawable.ic_league);
//        }

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            convertView = layoutinflater.inflate(R.layout.fragment_league_header, parent, false);
            holder = new HeaderViewHolder();
            holder.textView = convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        String text = "البطولات العالمية";
        if (position == 0)
            text = "البطولات العربية";
        // set header text as first char in string
        holder.textView.setText(text);

        return convertView;
    }

    @Override
    public int getCountForHeader(int header) {
        return header == 0 ? arabicCount : worldCount;
    }

    @Override
    public int getNumHeaders() {
        return 2;
    }

    private class ViewHolder {
        TextView name;
        ImageView logo;
    }

    private class HeaderViewHolder {
        public TextView textView;
    }

    public void setArabicCount(int arabicCount) {
        this.arabicCount = arabicCount;
    }

    public void setWorldCount(int worldCount) {
        this.worldCount = worldCount;
    }
}
