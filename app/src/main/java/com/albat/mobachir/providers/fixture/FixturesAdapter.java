package com.albat.mobachir.providers.fixture;

import android.content.Context;
import android.graphics.Color;

import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.albat.mobachir.Config;
import com.albat.mobachir.R;
import com.albat.mobachir.network.models.Fixture;
import com.albat.mobachir.network.models.Fixture.MatchStatus;
import com.albat.mobachir.network.models.League;
import com.albat.mobachir.util.CalendarHelper;
import com.albat.mobachir.util.SharedPreferencesManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class FixturesAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private ArrayList<League> leagues;
    private LayoutInflater inflater;
    Context context;
    SharedPreferencesManager sharedPreferencesManager;

    OnFixtureClickListener onFixtureClickListener;

    public FixturesAdapter(Context context, ArrayList<League> leagues, OnFixtureClickListener onFixtureClickListener) {
        this.leagues = leagues;
        inflater = LayoutInflater.from(context);
        this.context = context;
        sharedPreferencesManager = SharedPreferencesManager.getInstance(context);
        this.onFixtureClickListener = onFixtureClickListener;
    }

    @Override
    public int getCount() {
        int count = 0;
        for (League league : leagues)
            count += league.fixtures.size();

        return count;
    }

    @Override
    public Object getItem(int position) {
        int index = 0;
        for (League league : leagues)
            for (Fixture fixture : league.fixtures) {
                if (index == position)
                    return fixture;
                index++;
            }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.fragment_main_fixture_item, parent, false);
            holder.team1Picture = convertView.findViewById(R.id.team1Picture);
            holder.team1 = convertView.findViewById(R.id.team1);
            holder.team2Picture = convertView.findViewById(R.id.team2Picture);
            holder.team2 = convertView.findViewById(R.id.team2);
            holder.status = convertView.findViewById(R.id.status);
            holder.info = convertView.findViewById(R.id.info);
            holder.team1Result = convertView.findViewById(R.id.team1Result);
            holder.team2Result = convertView.findViewById(R.id.team2Result);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Fixture item = (Fixture) getItem(position);
        holder.team1.setText(item.team1);
        holder.team2.setText(item.team2);
        String info = CalendarHelper.reformatDateString(item.dateTime, Config.API_DATETIME_FORMAT, Config.APP_TIME_FORMAT, item.timezone, false);

        holder.status.setTextColor(Color.parseColor("#727272"));
        holder.info.setTextColor(Color.parseColor("#727272"));
        holder.team1Result.setTextColor(Color.parseColor("#727272"));
        holder.team2Result.setTextColor(Color.parseColor("#727272"));

        holder.team1Result.setVisibility(View.GONE);
        holder.team2Result.setVisibility(View.GONE);


        String matchStatus = item.getStatus();

        boolean live = false;
        switch (matchStatus) {
            case MatchStatus.LIVE:
            case MatchStatus.HALF_TIME:
            case MatchStatus.BREAK:
            case MatchStatus.EXTRA_TIME:
            case MatchStatus.PENALITY_LIVE:
                live = true;
                if (!item.manual) {
                    holder.team1Result.setVisibility(View.VISIBLE);
                    holder.team2Result.setVisibility(View.VISIBLE);

                    holder.team1Result.setText(item.team1Goals + "");
                    holder.team2Result.setText(item.team2Goals + "");
                    holder.info.setText(" - ");
                    if (item.team1PenGoals != 0 && item.team2PenGoals != 0) {
                        holder.team2Result.setText(item.team2Goals + " (" + item.team2PenGoals + ")");
                        holder.team1Result.setText("(" + item.team1PenGoals + ") " + item.team1Goals);
                    }
                    info = null;
                }
                holder.status.setTextColor(Color.parseColor("#2D8B19"));
                holder.info.setTextColor(Color.parseColor("#2D8B19"));
                holder.team1Result.setTextColor(Color.parseColor("#2D8B19"));
                holder.team2Result.setTextColor(Color.parseColor("#2D8B19"));
                break;
            case MatchStatus.ENDED_AFTER_FULL_TIME:
            case MatchStatus.ENDED_AFTER_EXTRA_TIME:
            case MatchStatus.ENDED_AFTER_PENALITY_SHOOTOUT:
            case MatchStatus.ENDED:
                live = false;
                if (!item.manual || (item.manual && item.resultAdded)) {
                    holder.team1Result.setVisibility(View.VISIBLE);
                    holder.team2Result.setVisibility(View.VISIBLE);

                    holder.team1Result.setText(item.team1Goals + "");
                    holder.team2Result.setText(item.team2Goals + "");
                    holder.info.setText(" - ");
                    if (item.team1PenGoals != 0 && item.team2PenGoals != 0) {
                        holder.team2Result.setText(item.team2Goals + " (" + item.team2PenGoals + ")");
                        holder.team1Result.setText("(" + item.team1PenGoals + ") " + item.team1Goals);
                    }
                    info = null;
                }
                break;
            case MatchStatus.NOT_STARTED:
            case MatchStatus.POSTPONED:
            case MatchStatus.DELAYED:
            case MatchStatus.CANCELED:
            case MatchStatus.SUSPENDED:
            case MatchStatus.INTERRUPTED:
                break;
        }

        if (info != null)
            holder.info.setText(info);
        holder.status.setText(item.getStatusٍString());
        if (live && item.stats != null && item.stats.minute != 0) {
            holder.status.setText(holder.status.getText() + "\n" + item.stats.minute + "'");
        }

        try {
            Picasso.with(context).load(item.team1Picture).error(R.drawable.ic_team_no_image).placeholder(R.drawable.ic_team_no_image).into(holder.team1Picture);
        } catch (Exception e) {
            holder.team1Picture.setImageResource(R.drawable.ic_team_no_image);
        }

        try {
            Picasso.with(context).load(item.team2Picture).error(R.drawable.ic_team_no_image).placeholder(R.drawable.ic_team_no_image).into(holder.team2Picture);
        } catch (Exception e) {
            holder.team2Picture.setImageResource(R.drawable.ic_team_no_image);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onFixtureClickListener != null)
                    onFixtureClickListener.onFixtureClicked((Fixture) getItem(position));
            }
        });

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        final HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.fragment_main_fixture_header, parent, false);
            holder.name = convertView.findViewById(R.id.name);
            holder.picture = convertView.findViewById(R.id.picture);
            holder.favorite = convertView.findViewById(R.id.favorite);

            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        //set header text as first char in name
        int index = 0;
        for (League league : leagues) {
            for (Fixture fixture : league.fixtures) {
                if (index == position) {
                    if (league.id == 0) {
                        holder.favorite.setVisibility(View.GONE);
                        holder.picture.setImageResource(R.drawable.ic_league);
                    } else {
                        holder.favorite.setVisibility(View.VISIBLE);
                        try {
                            Picasso.with(context).load(league.picture).error(R.drawable.ic_team_no_image).placeholder(R.drawable.ic_team_no_image).into(holder.picture);
                        } catch (Exception e) {
                            holder.picture.setImageResource(R.drawable.ic_team_no_image);
                        }
                    }

                    holder.name.setText(league.arName);
                    if (sharedPreferencesManager.isInsideFavorite(league.id))
                        holder.favorite.setColorFilter(Color.parseColor("#F1C40F"));
                    else
                        holder.favorite.setColorFilter(Color.GRAY);

                    holder.favorite.setTag(league);
                    holder.favorite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            League league = (League) view.getTag();
                            sharedPreferencesManager.addToFavoriteLeague(league.id);
                            notifyDataSetChanged();
                        }
                    });
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
        for (int i = 0; i < leagues.size(); i++) {
            for (Fixture fixture : leagues.get(i).fixtures) {
                if (index == position)
                    return i;
                index++;
            }
        }
        return -1;
    }

    class HeaderViewHolder {
        TextView name;
        CircleImageView picture;
        ImageView favorite;
    }

    class ViewHolder {
        CircleImageView team1Picture;
        TextView team1;
        CircleImageView team2Picture;
        TextView team2;
        TextView status;
        TextView info;
        TextView team1Result;
        TextView team2Result;
    }

    public interface OnFixtureClickListener {
        void onFixtureClicked(Fixture fixture);
    }
}