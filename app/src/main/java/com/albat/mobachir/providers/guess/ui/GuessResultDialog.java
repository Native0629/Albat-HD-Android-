package com.albat.mobachir.providers.guess.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.albat.mobachir.App;
import com.albat.mobachir.R;
import com.albat.mobachir.network.interfaces.GuessAdded;
import com.albat.mobachir.network.managers.UserManager;
import com.albat.mobachir.network.models.Fixture;
import com.albat.mobachir.network.models.Guess;
import com.albat.mobachir.network.models.MessageEvent;
import com.albat.mobachir.network.models.User;
import com.albat.mobachir.network.request.AddGuessRequest;
import com.albat.mobachir.util.DialogHelper;
import com.albat.mobachir.util.SharedPreferencesManager;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import de.hdodenhof.circleimageview.CircleImageView;


public class GuessResultDialog extends DialogFragment implements GuessAdded {
    Fixture fixture;

    ImageView incrementCoins, decrementCoins;
    TextView coinsBid;
    TextView resultFactor;
    TextView team1Goals, team2Goals;
    ImageView incrementTeam1Goals, incrementTeam2Goals, decrementTeam1Goals, decrementTeam2Goals;
    CircleImageView team1Picture, team2Picture;

    App app;
    DialogHelper dialogHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_result_guess_dialog, container, false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
        coinsBid.setText(fixture.minResultCoins + "");

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
                if (fixture.minResultCoins < currentCoins) {
                    currentCoins--;
                    coinsBid.setText(currentCoins + "");
                }
            }
        });

        resultFactor = view.findViewById(R.id.resultFactor);

        resultFactor.setText(fixture.goalsFactor + "x");

        team1Goals = view.findViewById(R.id.team1Goals);
        team2Goals = view.findViewById(R.id.team2Goals);

        incrementTeam1Goals = view.findViewById(R.id.incrementTeam1Goals);
        decrementTeam1Goals = view.findViewById(R.id.decrementTeam1Goals);
        incrementTeam1Goals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int goals = Integer.parseInt(team1Goals.getText().toString());
                goals++;
                team1Goals.setText(goals + "");
            }
        });
        decrementTeam1Goals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int goals = Integer.parseInt(team1Goals.getText().toString());
                if (goals > 0) {
                    goals--;
                    team1Goals.setText(goals + "");
                }
            }
        });

        incrementTeam2Goals = view.findViewById(R.id.incrementTeam2Goals);
        decrementTeam2Goals = view.findViewById(R.id.decrementTeam2Goals);
        incrementTeam2Goals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int goals = Integer.parseInt(team2Goals.getText().toString());
                goals++;
                team2Goals.setText(goals + "");
            }
        });
        decrementTeam2Goals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int goals = Integer.parseInt(team2Goals.getText().toString());
                if (goals > 0) {
                    goals--;
                    team2Goals.setText(goals + "");
                }
            }
        });

        view.findViewById(R.id.addGuess).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGuess();
            }
        });


        return view;
    }

    private void addGuess() {
        dialogHelper.showLoadingDialog(getActivity(), "جاري اضافة التوقع");

        AddGuessRequest addGuessRequest = new AddGuessRequest();
        addGuessRequest.team1Goals = Integer.parseInt(team1Goals.getText().toString());
        addGuessRequest.team2Goals = Integer.parseInt(team2Goals.getText().toString());
        addGuessRequest.resultBid = Integer.parseInt(coinsBid.getText().toString());
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

    public GuessResultDialog setFixture(Fixture fixture) {
        this.fixture = fixture;
        return this;
    }
}