package com.albat.mobachir.providers.guess.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.albat.mobachir.App;
import com.albat.mobachir.Config;
import com.albat.mobachir.R;
import com.albat.mobachir.network.interfaces.GuessAdded;
import com.albat.mobachir.network.managers.UserManager;
import com.albat.mobachir.network.models.Fixture;
import com.albat.mobachir.network.models.Guess;
import com.albat.mobachir.network.models.MessageEvent;
import com.albat.mobachir.network.models.User;
import com.albat.mobachir.network.request.AddGuessRequest;
import com.albat.mobachir.util.DialogHelper;
import com.albat.mobachir.util.MyToasty;
import com.albat.mobachir.util.SharedPreferencesManager;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import de.hdodenhof.circleimageview.CircleImageView;


public class GuessWinnerDialog extends DialogFragment implements GuessAdded {
    Fixture fixture;

    ImageView incrementCoins, decrementCoins;
    TextView coinsBid;
    TextView team1Factor, team2Factor, drawFactor;
    LinearLayout team1Layout, team2Layout, drawLayout;

    Integer fixtureGuessStatus = null;
    CircleImageView team1Picture, team2Picture;

    App app;
    DialogHelper dialogHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_winner_guess_dialog, container, false);

        getDialog
                ().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        app = App.getInstance();
        dialogHelper = DialogHelper.getInstance();

        team1Picture = view.findViewById(R.id.team1Picture);
        team2Picture = view.findViewById(R.id.team2Picture);

        try {
            Picasso.with(getActivity()).load(fixture.team1Picture).error(R.drawable.ic_team_no_image).placeholder(R.drawable.ic_team_no_image).into(team1Picture);
        } catch (Exception e) {
            team1Picture.setImageResource(R.drawable.ic_team_no_image);
        }

        try {
            Picasso.with(getActivity()).load(fixture.team2Picture).error(R.drawable.ic_team_no_image).placeholder(R.drawable.ic_team_no_image).into(team2Picture);
        } catch (Exception e) {
            team2Picture.setImageResource(R.drawable.ic_team_no_image);
        }
        coinsBid = view.findViewById(R.id.coinsBid);
        coinsBid.setText(fixture.minWinnerCoins + "");

        incrementCoins = view.findViewById(R.id.incrementCoins);
        decrementCoins = view.findViewById(R.id.decrementCoins);
        incrementCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentCoins = Integer.parseInt(coinsBid.getText().toString());
                if (app.getUser().goldenPoints > currentCoins) {
                    currentCoins++;
                    coinsBid.setText(currentCoins + "");
                }
            }
        });
        decrementCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentCoins = Integer.parseInt(coinsBid.getText().toString());
                if (fixture.minWinnerCoins < currentCoins) {
                    currentCoins--;
                    coinsBid.setText(currentCoins + "");
                }
            }
        });

        team1Factor = view.findViewById(R.id.team1Factor);
        team2Factor = view.findViewById(R.id.team2Factor);
        drawFactor = view.findViewById(R.id.drawFactor);

        team1Factor.setText(fixture.team1Factor + "x");
        team2Factor.setText(fixture.team2Factor + "x");
        drawFactor.setText(fixture.drawFactor + "x");

        team1Layout = view.findViewById(R.id.team1Layout);
        team2Layout = view.findViewById(R.id.team2Layout);
        drawLayout = view.findViewById(R.id.drawLayout);

        View.OnClickListener onSelectionChanged = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                team1Layout.setBackgroundColor(Color.TRANSPARENT);
                team2Layout.setBackgroundColor(Color.TRANSPARENT);
                drawLayout.setBackgroundColor(Color.TRANSPARENT);

                view.setBackgroundColor(Color.parseColor("#389025"));

                switch (view.getId()) {
                    case R.id.team1Layout:
                        fixtureGuessStatus = Config.FixtureGuessStatus.TEAM1WIN;
                        break;
                    case R.id.team2Layout:
                        fixtureGuessStatus = Config.FixtureGuessStatus.TEAM2WIN;
                        break;
                    case R.id.drawLayout:
                        fixtureGuessStatus = Config.FixtureGuessStatus.DRAW;
                        break;
                }
            }
        };
        team1Layout.setOnClickListener(onSelectionChanged);
        team2Layout.setOnClickListener(onSelectionChanged);
        drawLayout.setOnClickListener(onSelectionChanged);

        view.findViewById(R.id.addGuess).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGuess();
            }
        });


        return view;
    }

    private void addGuess() {
        if (fixtureGuessStatus == null) {
            MyToasty.info(getActivity(), "من فضلك اختر توقع أولا");
            return;
        }

        dialogHelper.showLoadingDialog(getActivity(), "جاري اضافة التوقع");

        AddGuessRequest addGuessRequest = new AddGuessRequest();
        addGuessRequest.fixtureStatus = fixtureGuessStatus;
        addGuessRequest.winnerBid = Integer.parseInt(coinsBid.getText().toString());
        addGuessRequest.userId = App.getInstance().getUser().id;
        addGuessRequest.fixtureId = fixture.id;
        addGuessRequest.manual = fixture.manual;

        UserManager userManager = new UserManager();
        userManager.addGuess(addGuessRequest, this);
    }

    @Override
    public void onGuessAdded(User user, Guess guess, boolean success, String error) {
        if (!success || user == null || guess == null) {
            dialogHelper.hideLoadingDialogError(getActivity(), "لم يتم اضافة التوقع", "حاول مرة أخرة");
            return;
        }

        dialogHelper.hideLoadingDialog();

        SharedPreferencesManager.getInstance(getActivity()).saveUser(user);
        App.getInstance().setUser(user);

        EventBus.getDefault().post(new MessageEvent(MessageEvent.UPDATE_COINS));
        EventBus.getDefault().post(guess);

        new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("تم اضافة التوقع")
                .setConfirmText("المتابعة")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        dismiss();
                    }
                })
                .show();
    }

    public GuessWinnerDialog setFixture(Fixture fixture) {
        this.fixture = fixture;
        return this;
    }
}