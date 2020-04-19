package com.albat.mobachir.providers.leagues;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.albat.mobachir.R;
import com.albat.mobachir.network.models.Stage;
import com.albat.mobachir.network.models.Standing;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class RegularStandingsAdapter extends BaseAdapter  {

    private ArrayList<Stage> stages;
    private LayoutInflater inflater;
    Context context;

    public RegularStandingsAdapter(Context context, ArrayList<Stage> stages) {
        this.stages = stages;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        int count = 0;
        for (Stage stage : stages)
            count += stage.standings.size();

        return count;
    }

    @Override
    public Object getItem(int position) {
        int index = 0;
        for (Stage stage : stages)
            for (Standing standing : stage.standings) {
                if (index == position)
                    return standing;
                index++;
            }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.fragment_standing_item, parent, false);
            holder.rank = convertView.findViewById(R.id.rank);
            holder.teamPicture = convertView.findViewById(R.id.teamPicture);
            holder.team = convertView.findViewById(R.id.team);
            holder.gamesPlayed = convertView.findViewById(R.id.gamesPlayed);
            holder.win = convertView.findViewById(R.id.win);
            holder.draw = convertView.findViewById(R.id.draw);
            holder.lose = convertView.findViewById(R.id.lose);
            holder.goalsScored = convertView.findViewById(R.id.goalsScored);
            holder.goalsAgainst = convertView.findViewById(R.id.goalsAgainst);
            holder.points = convertView.findViewById(R.id.points);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Standing standing = (Standing) getItem(position);
        holder.rank.setText(standing.rank + "");
        holder.team.setText(standing.arTeam);
        holder.gamesPlayed.setText(standing.gamesPlayed + "");
        holder.win.setText(standing.win + "");
        holder.draw.setText(standing.draw + "");
        holder.lose.setText(standing.lose + "");
        holder.goalsScored.setText(standing.goalsScored + "");
        holder.goalsAgainst.setText(standing.goalsAgainst + "");
        holder.points.setText(standing.points + "");

        try {
            Picasso.with(context).load(standing.teamPicture).error(R.drawable.news_placeholder).placeholder(R.drawable.news_placeholder).into(holder.teamPicture);
        } catch (Exception e) {
            holder.teamPicture.setImageResource(R.drawable.news_placeholder);
        }

        return convertView;
    }

    class ViewHolder {
        TextView rank;
        CircleImageView teamPicture;
        TextView team;
        TextView gamesPlayed;
        TextView win;
        TextView draw;
        TextView lose;
        TextView goalsScored;
        TextView goalsAgainst;
        TextView points;
    }

}