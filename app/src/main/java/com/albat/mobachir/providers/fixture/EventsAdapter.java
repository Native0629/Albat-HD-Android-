package com.albat.mobachir.providers.fixture;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.albat.mobachir.R;
import com.albat.mobachir.network.models.Event;
import com.albat.mobachir.util.InfiniteRecyclerViewAdapter;

import java.util.ArrayList;

public class EventsAdapter extends InfiniteRecyclerViewAdapter {
    private ArrayList<Event> events = new ArrayList<>();
    private Context context;

    public EventsAdapter(Context context) {
        super(context, null);
        this.context = context;
    }


    @Override
    protected int getViewType(int position) {
        return 0;
    }

    @Override
    protected RecyclerView.ViewHolder getViewHolder(ViewGroup parent, int viewType) {
        return new CViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_fixture_event_item, parent, false));
    }

    @Override
    protected void doBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Event event = events.get(position);

        CViewHolder viewHolder = (CViewHolder) holder;
        ImageView teamEventTypeIcon;
        TextView teamEventType;
        TextView teamDescription1;
        TextView teamDescription2;
        TextView teamDescription1Title;
        TextView teamDescription2Title;

        viewHolder.minute.setText(event.minute + "'");
        if (event.team == 2) {
            viewHolder.team1Description1Title.setVisibility(View.GONE);
            viewHolder.team1Description2Title.setVisibility(View.GONE);
            viewHolder.team1Description1.setVisibility(View.GONE);
            viewHolder.team1Description2.setVisibility(View.GONE);
            viewHolder.team1EventTypeIcon.setVisibility(View.GONE);
            viewHolder.team1EventType.setVisibility(View.GONE);

            teamDescription1 = viewHolder.team2Description1;
            teamDescription2 = viewHolder.team2Description2;
            teamEventTypeIcon = viewHolder.team2EventTypeIcon;
            teamEventType = viewHolder.team2EventType;
            teamDescription1Title = viewHolder.team2Description1Title;
            teamDescription2Title = viewHolder.team2Description2Title;
        } else {
            viewHolder.team2Description1Title.setVisibility(View.GONE);
            viewHolder.team2Description2Title.setVisibility(View.GONE);
            viewHolder.team2Description1.setVisibility(View.GONE);
            viewHolder.team2Description2.setVisibility(View.GONE);
            viewHolder.team2EventTypeIcon.setVisibility(View.GONE);
            viewHolder.team2EventType.setVisibility(View.GONE);

            teamDescription1 = viewHolder.team1Description1;
            teamDescription2 = viewHolder.team1Description2;
            teamEventTypeIcon = viewHolder.team1EventTypeIcon;
            teamEventType = viewHolder.team1EventType;
            teamDescription1Title = viewHolder.team1Description1Title;
            teamDescription2Title = viewHolder.team1Description2Title;
        }

        teamDescription1.setVisibility(View.VISIBLE);
        teamDescription2.setVisibility(View.VISIBLE);
        teamEventTypeIcon.setVisibility(View.VISIBLE);
        teamEventType.setVisibility(View.VISIBLE);

        switch (event.type) {
            case 1:
                teamEventTypeIcon.setImageResource(R.drawable.ic_goal);
                teamEventType.setText("جــــول");
                teamDescription1.setText(event.meta1);
                teamDescription2.setVisibility(View.GONE);
                teamDescription1Title.setVisibility(View.GONE);
                teamDescription2Title.setVisibility(View.GONE);
                break;
            case 2:
                teamDescription2.setVisibility(View.GONE);
                teamDescription1Title.setVisibility(View.GONE);
                teamDescription2Title.setVisibility(View.GONE);
                teamDescription1.setText(event.meta1);
                if (event.description.equalsIgnoreCase("yellowcard")) {
                    teamEventType.setText("بطاقة صفراء");
                    teamEventTypeIcon.setImageResource(R.drawable.ic_yellow_card);
                } else {
                    teamEventType.setText("بطاقة حمراء");
                    teamEventTypeIcon.setImageResource(R.drawable.ic_red_card);
                }
                break;
            case 3:
                teamEventTypeIcon.setImageResource(R.drawable.ic_substitute);
                teamDescription2.setVisibility(View.VISIBLE);
                teamDescription1Title.setVisibility(View.VISIBLE);
                teamDescription2Title.setVisibility(View.VISIBLE);
                teamEventType.setText("تبديــل");
                teamDescription1Title.setText("دخول:");
                teamDescription1.setText(event.meta1);
                teamDescription2Title.setText("خروج:");
                teamDescription2.setText(event.meta2);
                break;
        }
    }

    @Override
    protected int getCount() {
        return events.size();
    }

    private class CViewHolder extends RecyclerView.ViewHolder {
        public TextView minute;


        public ImageView team1EventTypeIcon;
        public TextView team1EventType;
        public TextView team1Description1;
        public TextView team1Description2;
        public TextView team1Description1Title;
        public TextView team1Description2Title;

        public ImageView team2EventTypeIcon;
        public TextView team2EventType;
        public TextView team2Description1;
        public TextView team2Description2;
        public TextView team2Description1Title;
        public TextView team2Description2Title;

        public View itemView;

        private CViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            minute = itemView.findViewById(R.id.minute);

            team1EventTypeIcon = itemView.findViewById(R.id.team1EventTypeIcon);
            team1EventType = itemView.findViewById(R.id.team1EventType);
            team1Description1 = itemView.findViewById(R.id.team1Description1);
            team1Description2 = itemView.findViewById(R.id.team1Description2);
            team1Description1Title = itemView.findViewById(R.id.team1Description1Title);
            team1Description2Title = itemView.findViewById(R.id.team1Description2Title);

            team2EventTypeIcon = itemView.findViewById(R.id.team2EventTypeIcon);
            team2EventType = itemView.findViewById(R.id.team2EventType);
            team2Description1 = itemView.findViewById(R.id.team2Description1);
            team2Description2 = itemView.findViewById(R.id.team2Description2);
            team2Description1Title = itemView.findViewById(R.id.team2Description1Title);
            team2Description2Title = itemView.findViewById(R.id.team2Description2Title);
        }
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}