package com.albat.mobachir.providers.news.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.albat.mobachir.BaseFragment;
import com.albat.mobachir.MainActivity;
import com.albat.mobachir.R;
import com.albat.mobachir.network.interfaces.NewsRetrieved;
import com.albat.mobachir.network.managers.NewsManager;
import com.albat.mobachir.network.models.MessageEvent;
import com.albat.mobachir.network.models.News;
import com.albat.mobachir.providers.news.NewsAdapter;
import com.albat.mobachir.util.InfiniteRecyclerViewAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * This file is part of the Universal template
 * For license information, please check the LICENSE
 * file in the root of this project
 *
 * @author Sherdle
 * Copyright 2017
 */
public class NewsFragment extends BaseFragment implements NewsAdapter.OnOverViewClick, NewsRetrieved {
    String TAG = "NewsFragment";
    //Views
    private RecyclerView recyclerView;
    private FloatingActionButton refreshFab;
    private ArrayList<News> items = new ArrayList<>();

    //List
    private NewsAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_news, null);
        setHasOptionsMenu(true);
        recyclerView = view.findViewById(R.id.list);

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        items = new ArrayList<>();
        adapter = new NewsAdapter(items, getContext(), NewsFragment.this);
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

                refreshFab.setVisibility(View.GONE);
                adapter.clear();
                adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_PROGRESS);
                loadItems();
            }
        });
        refreshFab.setVisibility(View.GONE);
        //Load items
        loadItems();

        return view;
    }

    public void loadItems() {
        adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_PROGRESS);
        NewsManager newsManager = new NewsManager();
        newsManager.getNews(this);
    }

    @Override
    public void onNewsRetrieved(ArrayList<News> news, boolean success, String error) {
        if (!success || news == null) {
            Toast.makeText(getActivity(), R.string.invalid_configuration, Toast.LENGTH_LONG).show();
            adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_EMPTY);
            return;
        } else {
            //Add all the new posts to the list and notify the adapter
            items.clear();
            items.addAll(news);
            if (items.isEmpty())
                adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_EMPTY);
            else
                adapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_LIST);
        }

        //refreshFab.setVisibility(View.VISIBLE);
    }

    @Override
    public void onOverViewSelected(News item) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showInterstitial();
        }

        Fragment fragment = new ViewNewsFragment().setItem(item);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();

        EventBus.getDefault().post(new MessageEvent(MessageEvent.SHOW_INTERSTITIAL_AD));
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
