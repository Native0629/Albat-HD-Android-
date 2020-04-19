package com.albat.mobachir.providers.news.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.albat.mobachir.BaseFragment;
import com.albat.mobachir.MainActivity;
import com.albat.mobachir.R;
import com.albat.mobachir.network.interfaces.CommentsRetrieved;
import com.albat.mobachir.network.managers.NewsManager;
import com.albat.mobachir.network.models.Comment;
import com.albat.mobachir.network.models.MessageEvent;
import com.albat.mobachir.network.models.News;
import com.albat.mobachir.providers.news.CommentsAdapter;
import com.albat.mobachir.util.InfiniteRecyclerViewAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class CommentsFragment extends BaseFragment implements  CommentsRetrieved {
    String TAG = "CommentsFragment";
    //Views
    private RecyclerView recyclerView;
    private FloatingActionButton refreshFab;
    private News news;
    private ArrayList<Comment> items = new ArrayList<>();

    //List
    private CommentsAdapter adapter;
    private TextView noComments;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_comments, null);
        setHasOptionsMenu(true);
        recyclerView = view.findViewById(R.id.list);
        noComments = view.findViewById(R.id.noComments);

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        items = new ArrayList<>();
        adapter = new CommentsAdapter(items, getContext());
        recyclerView.setAdapter(adapter);
        adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_PROGRESS);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        refreshFab = view.findViewById(R.id.refresh);
        refreshFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).showInterstitial();
                }
                noComments.setVisibility(View.GONE);
                refreshFab.setVisibility(View.GONE);
                adapter.clear();
                adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_PROGRESS);
                loadItems();
            }
        });

        //Load items
        loadItems();

        return view;
    }

    public void loadItems() {
        NewsManager newsManager = new NewsManager();
        newsManager.getNewsComments(news.id, this);
    }

    @Override
    public void onCommentsRetrieved(ArrayList<Comment> comments, boolean success, String error) {
        if (!success || comments == null) {
            Toast.makeText(getActivity(), R.string.invalid_configuration, Toast.LENGTH_LONG).show();
            adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_EMPTY);
            noComments.setVisibility(View.GONE);
            return;
        } else {
            //Add all the new posts to the list and notify the adapter
            items.clear();
            items.addAll(comments);
            if (items.isEmpty()) {
                noComments.setVisibility(View.VISIBLE);
                adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_EMPTY);
            } else {
                noComments.setVisibility(View.GONE);
                adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_LIST);
            }
        }

        refreshFab.setVisibility(View.VISIBLE);
    }


    public CommentsFragment setNews(News news) {
        this.news = news;
        return this;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        switch (messageEvent.getCode()) {
            case MessageEvent.REFRESH:
                loadItems();
                break;
        }
    }
}
