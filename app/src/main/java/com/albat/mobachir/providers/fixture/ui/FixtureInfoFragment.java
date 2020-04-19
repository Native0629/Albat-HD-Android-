package com.albat.mobachir.providers.fixture.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.albat.mobachir.App;
import com.albat.mobachir.BaseFragment;
import com.albat.mobachir.LoginActivity;
import com.albat.mobachir.R;
import com.albat.mobachir.network.models.Fixture;
import com.albat.mobachir.network.models.Fixture.MatchStatus;
import com.albat.mobachir.network.models.Guess;
import com.albat.mobachir.network.models.MessageEvent;
import com.albat.mobachir.providers.guess.ui.GuessResultDialog;
import com.albat.mobachir.providers.guess.ui.GuessWinnerDialog;
import com.albat.mobachir.util.SharedPreferencesManager;
import com.github.florent37.shapeofview.shapes.RoundRectView;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FixtureInfoFragment extends BaseFragment {
    String TAG = "FixtureInfoFragment";
    //Views
    private Fixture fixture;

    boolean isInitialized = false;

    TextView league, channel, commentator, stadium;

    public LinearLayout guessResult;
    public LinearLayout guessWinner;
    public TextView guessResultMinCoins;
    public TextView guessWinnerMinCoins;

    RoundRectView guessLayout, signinLayout;

    SharedPreferencesManager sharedPreferencesManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_fixture_info, null);
        setHasOptionsMenu(true);

        sharedPreferencesManager = SharedPreferencesManager.getInstance(getActivity());

        view.findViewById(R.id.signinButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        guessResult = view.findViewById(R.id.guessResult);
        guessWinner = view.findViewById(R.id.guessWinner);
        guessResultMinCoins = view.findViewById(R.id.guessResultMinCoins);
        guessWinnerMinCoins = view.findViewById(R.id.guessWinnerMinCoins);
        guessLayout = view.findViewById(R.id.guessLayout);
        signinLayout = view.findViewById(R.id.signinLayout);

        league = view.findViewById(R.id.league);
        channel = view.findViewById(R.id.channel);
        commentator = view.findViewById(R.id.commentator);
        stadium = view.findViewById(R.id.stadium);


        isInitialized = true;
        if (fixture != null)
            loadItems();

        return view;
    }

    public void loadItems() {
        channel.setText(fixture.channel.isEmpty() ? "لم يحدد بعد" : fixture.channel);
        commentator.setText(fixture.commentator.isEmpty() ? "لم يحدد بعد" : fixture.commentator);
        stadium.setText(fixture.stadium.isEmpty() ? "لم يحدد بعد" : fixture.stadium);
        league.setText(fixture.league == null ? "~" : fixture.league.arName);

        if (!sharedPreferencesManager.isLoggedIn()) {
            guessLayout.setVisibility(View.GONE);
            signinLayout.setVisibility(View.VISIBLE);

            return;
        } else {
            signinLayout.setVisibility(View.GONE);
        }

        switch (fixture.getStatus()) {
            case MatchStatus.NOT_STARTED:
                guessLayout.setVisibility(View.VISIBLE);
                break;
            case MatchStatus.POSTPONED:
            case MatchStatus.DELAYED:
            case MatchStatus.CANCELED:
            case MatchStatus.SUSPENDED:
            case MatchStatus.INTERRUPTED:
            case MatchStatus.LIVE:
            case MatchStatus.HALF_TIME:
            case MatchStatus.BREAK:
            case MatchStatus.EXTRA_TIME:
            case MatchStatus.PENALITY_LIVE:
            case MatchStatus.ENDED_AFTER_FULL_TIME:
            case MatchStatus.ENDED_AFTER_EXTRA_TIME:
            case MatchStatus.ENDED_AFTER_PENALITY_SHOOTOUT:
            case MatchStatus.ENDED:
                guessLayout.setVisibility(View.GONE);
                break;
        }

        guessResultMinCoins.setText(fixture.minResultCoins + "");
        guessWinnerMinCoins.setText(fixture.minWinnerCoins + "");

        if (fixture.guessWinnerAvailable == null || !fixture.guessWinnerAvailable) {
            guessWinner.setEnabled(false);
            guessWinner.setAlpha(0.3f);
        } else {
            guessWinner.setEnabled(true);
            guessWinner.setAlpha(1f);
        }
        guessWinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (App.getInstance().getUser().goldenPoints < fixture.minWinnerCoins) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("قطع ذهبية غير كافية")
                            .setContentText("مشاهدة إعلان لإضافة قطع ذهبية؟")
                            .setConfirmText("مشاهدة")
                            .setCancelText("لا")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    EventBus.getDefault().post(new MessageEvent(MessageEvent.WATCH_ADD));
                                }
                            })
                            .show();
                } else {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    Fragment prev = getFragmentManager().findFragmentByTag("guessWinnerDialog");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);
                    DialogFragment dialogFragment = new GuessWinnerDialog().setFixture(fixture);
                    dialogFragment.show(ft, "guessWinnerDialog");
                }
            }
        });

        if (fixture.guessResultAvailable == null || !fixture.guessResultAvailable) {
            guessResult.setEnabled(false);
            guessResult.setAlpha(0.3f);
        } else {
            guessResult.setEnabled(true);
            guessResult.setAlpha(1f);
        }
        guessResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (App.getInstance().getUser().goldenPoints < fixture.minResultCoins) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("قطع ذهبية غير كافية")
                            .setContentText("مشاهدة إعلان لإضافة قطع ذهبية؟")
                            .setConfirmText("مشاهدة")
                            .setCancelText("لا")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    EventBus.getDefault().post(new MessageEvent(MessageEvent.WATCH_ADD));
                                }
                            })
                            .show();
                } else {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    Fragment prev = getFragmentManager().findFragmentByTag("guessResultDialog");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);
                    DialogFragment dialogFragment = new GuessResultDialog().setFixture(fixture);
                    dialogFragment.show(ft, "guessResultDialog");
                }
            }
        });
    }


    public void setFixture(Fixture fixture) {
        this.fixture = fixture;
        if (isInitialized)
            loadItems();
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
    public void onGuessAdded(Guess guess) {
        if (fixture != null) {
            if (fixture.id == guess.fixtureId && fixture.manual == guess.manual) {
                if (guess.winnerBid != null && guess.resultBid != null) {
                    fixture.guessWinnerAvailable = false;
                    fixture.guessResultAvailable = false;
                } else if (guess.winnerBid != null)
                    fixture.guessWinnerAvailable = false;
                else if (guess.resultBid != null)
                    fixture.guessResultAvailable = false;
                loadItems();
                return;
            }
        }
    }
}
