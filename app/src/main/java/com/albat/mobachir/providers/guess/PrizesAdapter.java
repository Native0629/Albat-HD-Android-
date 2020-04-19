package com.albat.mobachir.providers.guess;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.albat.mobachir.R;
import com.albat.mobachir.network.models.Prize;
import com.albat.mobachir.util.InfiniteRecyclerViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PrizesAdapter extends InfiniteRecyclerViewAdapter {
    private ArrayList<Prize> data;
    private Context context;

    public PrizesAdapter(ArrayList<Prize> data, Context context) {
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
        return new CViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_prize_list_item, parent, false));
    }

    @Override
    protected void doBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Prize item = data.get(position);
        CViewHolder viewHolder = (CViewHolder) holder;
        viewHolder.name.setText(item.name);

        try {
            Picasso.with(context).load(item.picture).error(R.drawable.ic_gift).placeholder(R.drawable.ic_gift).into(viewHolder.picture);
        } catch (Exception e) {
            viewHolder.picture.setImageResource(R.drawable.ic_gift);
        }
    }

    @Override
    protected int getCount() {
        return data.size();
    }

    private class CViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView picture;

        public View itemView;

        private CViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            name = itemView.findViewById(R.id.name);
            picture = itemView.findViewById(R.id.picture);
        }
    }

    public void clear() {
        data.clear();
    }
}