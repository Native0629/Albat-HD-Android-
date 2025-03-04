package com.albat.mobachir.providers.woocommerce.ui;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.albat.mobachir.Config;
import com.albat.mobachir.HolderActivity;
import com.albat.mobachir.MainActivity;
import com.albat.mobachir.R;
import com.albat.mobachir.providers.woocommerce.WooCommerceTask;
import com.albat.mobachir.providers.woocommerce.adapter.ProductsAdapter;
import com.albat.mobachir.providers.woocommerce.model.products.Category;
import com.albat.mobachir.providers.woocommerce.model.products.Image;
import com.albat.mobachir.providers.woocommerce.model.products.Product;
import com.albat.mobachir.util.Helper;
import com.albat.mobachir.util.InfiniteRecyclerViewAdapter;
import com.albat.mobachir.util.Log;
import com.albat.mobachir.util.layout.StaggeredGridSpacingItemDecoration;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * This file is part of the Universal template
 * For license information, please check the LICENSE
 * file in the root of this project
 *
 * @author Sherdle
 * Copyright 2017
 */
public class WooCommerceFragment extends Fragment implements WooCommerceTask.Callback<Product>, InfiniteRecyclerViewAdapter.LoadMoreListener {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProductsAdapter productsAdapter;
    private List<Product> productList;
    private Activity mAct;

    private int page = 1;
    private int SPAN_COUNT = 2;

    private int category;
    private String searchQuery;

    public WooCommerceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wc, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        recyclerView = view.findViewById(R.id.recycleView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        productList = new ArrayList<>();
        productsAdapter = new ProductsAdapter(getContext(), productList, this);
        productsAdapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_PROGRESS);
        recyclerView.setAdapter(productsAdapter);

        mAct = getActivity();
        String[] args = this.getArguments().getStringArray(MainActivity.FRAGMENT_DATA);
        if (args.length > 0 && args[0].matches("^-?\\d+$")){
            category = Integer.parseInt(args[0]);
        }

        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new StaggeredGridSpacingItemDecoration((int) getResources().getDimension(R.dimen.woocommerce_padding), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        
        if (getString(R.string.woocommerce_url).isEmpty() || !getString(R.string.woocommerce_url).startsWith("http")) {
            Toast.makeText(mAct, "You need to enter a valid WooCommerce url and API tokens as documented!", Toast.LENGTH_SHORT).show();
            return;
        }


        refreshItems();
        loadCategorySlider();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });
    }


    private void loadCategorySlider(){
        if (category != 0 || !Config.WC_CHIPS) return;

        WooCommerceTask.Callback<Category> callback = new WooCommerceTask.Callback<Category>() {
            @Override
            public void success(ArrayList<Category> categories) {
                LayoutInflater layoutInflater = LayoutInflater.from(mAct);

                final ViewGroup sliderView = (ViewGroup) layoutInflater.inflate(R.layout.fragment_wc_slider, null);
                for (final Category item : categories) {
                    ViewGroup cardView;
                    if (item.getImage() != null && item.getImage() instanceof JsonObject) {
                        Image image = new Gson().fromJson(item.getImage(), Image.class);
                        cardView = (ViewGroup) layoutInflater.inflate(R.layout.fragment_wc_category_card_image, null);
                        Picasso.with(mAct).load(image.getSrc()).into((ImageView) cardView.findViewById(R.id.image));
                    } else {
                        cardView = (ViewGroup) layoutInflater.inflate(R.layout.fragment_wc_category_card_text, null);
                        cardView.findViewById(R.id.background).setBackgroundResource(randomGradientResource(categories.indexOf(item)));
                    }

                    TextView title = cardView.findViewById(R.id.title);
                    title.setText(item.getName());
                    cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            HolderActivity.startActivity(mAct, WooCommerceFragment.class, new String[]{(Integer.toString(item.getId()))});
                        }
                    });
                    ((LinearLayout) sliderView.findViewById(R.id.slider_content)).addView(cardView);
                }

                productsAdapter.setSlider(sliderView);

                //Animate the appearance
                sliderView.setAlpha(0);
                sliderView.animate().alpha(1).setDuration(500).start();
            }

            @Override
            public void failed() {

            }
        };

        new WooCommerceTask.WooCommerceBuilder(mAct).getCategories(callback).execute();
    }

    private int randomGradientResource(int index){
        index += 1;
        if (index == 6) index = 1;

        return Helper.getGradient(index);
    }

    @Override
    public void success(ArrayList<Product> result) {
        if (result.size() > 0) {
            productList.addAll(result);
        } else {
            productsAdapter.setHasMore(false);
        }
        productsAdapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_LIST);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void failed() {
        productsAdapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_EMPTY);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void refreshItems() {
        // Load items
        page = 1;
        productList.clear();
        productsAdapter.setHasMore(true);
        productsAdapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_PROGRESS);
        requestItems();
    }

    @Override
    public void onMoreRequested() {
        page = page + 1;
        requestItems();
    }

    private void requestItems(){
        WooCommerceTask.WooCommerceBuilder builder = new WooCommerceTask.WooCommerceBuilder(mAct);
        if (searchQuery != null)
            builder.getProductsForQuery(this, searchQuery, page).execute();
        else if (category != 0)
            builder.getProductsForCategory(this, category, page).execute();
        else
            builder.getProducts(this, page).execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.woocommerce_menu, menu);

        // set & get the search button in the actionbar
        final SearchView searchView = new SearchView(getActivity());
        searchView.setQueryHint(getResources().getString(
                R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    query = URLEncoder.encode(query, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    Log.printStackTrace(e);
                }
                searchView.clearFocus();

                searchQuery = query;
                refreshItems();
                productsAdapter.setSlider(null);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });

        searchView .addOnAttachStateChangeListener(
                new View.OnAttachStateChangeListener() {

                    @Override
                    public void onViewDetachedFromWindow(View arg0) {
                        searchQuery = null;
                        refreshItems();
                    }

                    @Override
                    public void onViewAttachedToWindow(View arg0) {
                        // search was opened
                    }
                });

        //TODO make menu an xml item
        menu.add(R.string.search)
                .setIcon(R.drawable.ic_action_search)
                .setActionView(searchView)
                .setShowAsAction(
                        MenuItem.SHOW_AS_ACTION_IF_ROOM
                                | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_cart:
                HolderActivity.startActivity(getActivity(), CartFragment.class, null);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}