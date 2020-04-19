package com.albat.mobachir.providers.leagues.ui;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.albat.mobachir.BaseFragment;
import com.albat.mobachir.R;
import com.albat.mobachir.network.interfaces.LeaguesRetrieved;
import com.albat.mobachir.network.managers.LeaguesManager;
import com.albat.mobachir.network.models.League;
import com.albat.mobachir.network.models.LeaguesData;
import com.albat.mobachir.network.models.MessageEvent;
import com.albat.mobachir.util.DialogHelper;
import com.albat.mobachir.util.SharedPreferencesManager;
import com.kaushikthedeveloper.squarelayout.SquareLinearLayout;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;


public class LeaguesFragment extends BaseFragment implements LeaguesRetrieved, View.OnClickListener {
    String TAG = "LeaguesFragment";
    //Views
    DialogHelper dialogHelper;
    SharedPreferencesManager sharedPreferencesManager;

    LinearLayout importantLeaguesLayout, arabicLeaguesLayout, euroLeaguesLayout, africanLeaguesLayout, asianLeaguesLayout, worldLeaguesLayout;
    LinearLayout importantLeaguesCategory, arabicLeaguesCategory, euroLeaguesCategory, africanLeaguesCategory, asianLeaguesCategory, worldLeaguesCategory;

    TextView loading;

    LeaguesData leaguesData = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_leagues, null);
        setHasOptionsMenu(true);

        dialogHelper = new DialogHelper();
        sharedPreferencesManager = SharedPreferencesManager.getInstance(getActivity());

        arabicLeaguesLayout = view.findViewById(R.id.arabicLeaguesLayout);
        worldLeaguesLayout = view.findViewById(R.id.worldLeaguesLayout);
        euroLeaguesLayout = view.findViewById(R.id.euroLeaguesLayout);
        africanLeaguesLayout = view.findViewById(R.id.africanLeaguesLayout);
        asianLeaguesLayout = view.findViewById(R.id.asianLeaguesLayout);
        importantLeaguesLayout = view.findViewById(R.id.importantLeaguesLayout);

        arabicLeaguesCategory = view.findViewById(R.id.arabicLeaguesCategory);
        euroLeaguesCategory = view.findViewById(R.id.euroLeaguesCategory);
        africanLeaguesCategory = view.findViewById(R.id.africanLeaguesCategory);
        asianLeaguesCategory = view.findViewById(R.id.asianLeaguesCategory);
        worldLeaguesCategory = view.findViewById(R.id.worldLeaguesCategory);
        importantLeaguesCategory = view.findViewById(R.id.importantLeaguesCategory);

        loading = view.findViewById(R.id.loading);

        getLeagues();

        return view;
    }

    public void getLeagues() {
        loading.setText("جاري التحميل");
        loading.setVisibility(View.VISIBLE);
        //dialogHelper.showLoadingDialog(getActivity(), "جاري التحميل");
        LeaguesManager leaguesManager = new LeaguesManager();
        leaguesManager.getLeagues(this);
    }

    @Override
    public void onLeaguesRetrieved(final LeaguesData leaguesData, boolean success, String error) {
        if (!success || leaguesData == null) {
            //dialogHelper.hideLoadingDialogError(getActivity(), "فشل التحميل", error);
            loading.setVisibility(View.VISIBLE);
            loading.setText("برجاء المحاولة مره أخره");
            return;
        }

        //dialogHelper.hideLoadingDialog();
        this.leaguesData = leaguesData;

        fillData(leaguesData.importantLeagues, importantLeaguesCategory, importantLeaguesLayout);

        fillData(leaguesData.arabicLeagues, arabicLeaguesCategory, arabicLeaguesLayout);
        fillData(leaguesData.euroLeagues, euroLeaguesCategory, euroLeaguesLayout);
        fillData(leaguesData.africanLeagues, africanLeaguesCategory, africanLeaguesLayout);
        fillData(leaguesData.asianLeagues, asianLeaguesCategory, asianLeaguesLayout);
        fillData(leaguesData.worldLeagues, worldLeaguesCategory, worldLeaguesLayout);

        loading.setVisibility(View.GONE);
    }

    private void fillData(ArrayList<League> leagues, LinearLayout holderLayout, LinearLayout itemsLayout) {
        if (leagues.isEmpty()) {
            holderLayout.setVisibility(View.GONE);
        } else {
            holderLayout.setVisibility(View.VISIBLE);
            itemsLayout.removeAllViews();

            LinearLayout row = createRow();
            for (int i = 0; i < leagues.size(); i++) {
                if (i % 3 == 0) { // Add Row
                    itemsLayout.addView(row);
                    row = createRow();
                }
                row.addView(createItem(leagues.get(i), i + 1 % 3 != 0));
            }
            if (row.getChildCount() != 0) {
                int count = row.getChildCount();
                while (count < 3) {
                    row.addView(createItem(null, row.getChildCount() < 2));
                    count++;
                }
                itemsLayout.addView(row);
            }
        }

    }

    private LinearLayout createRow() {
        LinearLayout row = new LinearLayout(getActivity());
        row.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = 20;
        row.setLayoutParams(lp);
        return row;
    }


    private View createItem(final League league, boolean addMargin) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        if (addMargin)
            layoutParams.rightMargin = 20;

        if (league == null) {
            SquareLinearLayout dummy = new SquareLinearLayout(getActivity());
            dummy.setLayoutParams(layoutParams);
            return dummy;
        }

        SquareLinearLayout item = (SquareLinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.fragment_leagues_item, null);
        item.setLayoutParams(layoutParams);
        item.setOnClickListener(this);
        item.setTag(league);
        TextView name = item.findViewById(R.id.name);
        final ImageView picture = item.findViewById(R.id.picture);
        name.setText(league.arName);
        picture.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (league.picture != null && !league.picture.isEmpty()) {
                        Picasso.with(getContext().getApplicationContext()).load(league.picture).error(R.drawable.ic_league).placeholder(R.drawable.ic_league).into(picture);
                    }
                } catch (Exception e) {
                    picture.setImageResource(R.drawable.ic_league);
                }
            }
        }, 1000);

        return item;
    }

    @Override
    public void onClick(View view) {
        League league = (League) view.getTag();

        Fragment fragment = new ViewLeagueFragment().setLeague(league);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
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
                if (leaguesData == null)
                    getLeagues();
                else {
                    loading.setText("جاري التحميل");
                    loading.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loading.setVisibility(View.GONE);
                        }
                    }, 500);
                    break;
                }
        }
    }
}
