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
import com.albat.mobachir.util.CLog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class StandingsAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    String TAG = "StandingsAdapter";
    private ArrayList<Stage> stages;
    private LayoutInflater inflater;
    Context context;

    public StandingsAdapter(Context context, ArrayList<Stage> stages) {
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

    public Stage getStageForStandingAt(int position) {
        int index = 0;
        for (Stage stage : stages)
            for (Standing standing : stage.standings) {
                if (index == position)
                    return stage;
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
            holder.groupName = convertView.findViewById(R.id.groupName);

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

        Stage stage = getStageForStandingAt(position);
        if (standing.groupId == null || standing.groupName == null) {
            holder.groupName.setVisibility(View.GONE);
        } else {
            int subIndex = -1;
            for (int i = 0; i < stage.standings.size(); i++) {
                if (stage.standings.get(i).id == standing.id) {
                    subIndex = i;
                    break;
                }
            }

            boolean showGroupName = false;
            if (subIndex == -1)
                showGroupName = false;
            else if (subIndex == 0)
                showGroupName = true;
            else {
                Standing previousStanding = stage.standings.get(subIndex - 1);
                if (previousStanding.groupId != null && !previousStanding.groupId.equals(standing.groupId)) {
                    showGroupName = true;
                }
            }
            if (showGroupName) {
                holder.groupName.setText(standing.groupName);
                holder.groupName.setVisibility(View.VISIBLE);
            } else
                holder.groupName.setVisibility(View.GONE);
        }

        try {
            Picasso.with(context).load(standing.teamPicture).error(R.drawable.ic_team_no_image).placeholder(R.drawable.ic_team_no_image).into(holder.teamPicture);
        } catch (Exception e) {
            holder.teamPicture.setImageResource(R.drawable.ic_team_no_image);
        }

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            TextView textView = new TextView(context);
            textView.setVisibility(View.VISIBLE);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setPadding(5, 5, 5, 5);
            textView.setBackgroundColor(Color.LTGRAY);
            textView.setTextColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER);
            convertView = textView;
            holder.text = (TextView) convertView;

            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        //set header text as first char in name
        int index = 0;
        for (Stage stage : stages) {
            for (Standing standing : stage.standings) {
                if (index == position) {
                    holder.text.setText(stage.arName);
                    return convertView;
                }
                index++;
            }
        }

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        //return the first character of the country as ID because this is what headers are based upon
        int index = 0;
        for (int i = 0; i < stages.size(); i++) {
            for (Standing standing : stages.get(i).standings) {
                if (index == position)
                    return i;
                index++;
            }
        }
        return -1;
    }

    class HeaderViewHolder {
        TextView text;
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
        TextView groupName;
    }

}