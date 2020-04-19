package com.albat.mobachir.providers.guess.ui;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.albat.mobachir.App;
import com.albat.mobachir.BaseFragment;
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
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AddFixtureGuessFragment extends BaseFragment implements GuessAdded {
    String TAG = "AddFixtureGuessFragment";
    DialogHelper dialogHelper;

    Fixture fixture;
    Integer fixtureGuessStatus = null;

    Button draw, team1Win, team2Win;
    Button yes, no;
    boolean includeGoalsGuess = false;

    TextView goldenPoints, team1, team2, team1Name, team2Name, team1Factor, team2Factor, goalsFactor;
    ImageView team1Picture, team2Picture;
    ElegantNumberButton team1Goals, team2Goals;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_add_fixture_guess, null);
        setHasOptionsMenu(true);

        dialogHelper = DialogHelper.getInstance();

        goldenPoints = view.findViewById(R.id.goldenPoints);
        goldenPoints.setText(App.getInstance().getUser().goldenPoints + "");

        team1Name = view.findViewById(R.id.team1Name);
        team2Name = view.findViewById(R.id.team2Name);
        team1 = view.findViewById(R.id.team1);
        team2 = view.findViewById(R.id.team2);
        team1Factor = view.findViewById(R.id.team1Factor);
        team2Factor = view.findViewById(R.id.team2Factor);
        goalsFactor = view.findViewById(R.id.goalsFactor);
        team1Picture = view.findViewById(R.id.team1Flag);
        team2Picture = view.findViewById(R.id.team2Flag);

        team1Goals = view.findViewById(R.id.team1Goals);
        team2Goals = view.findViewById(R.id.team2Goals);

        draw = view.findViewById(R.id.draw);
        team1Win = view.findViewById(R.id.team1Win);
        team2Win = view.findViewById(R.id.team2Win);

        View.OnClickListener onSelectGuessFixtureStatusPressed = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedTextColor = Color.WHITE;
                int selectedBackground = ContextCompat.getColor(getActivity(), R.color.colorAccent);
                int normalTextColor = Color.BLACK;
                int normalBackground = Color.TRANSPARENT;

                draw.setTextColor(normalTextColor);
                draw.setBackgroundColor(normalBackground);

                team1Win.setTextColor(normalTextColor);
                team1Win.setBackgroundColor(normalBackground);

                team2Win.setTextColor(normalTextColor);
                team2Win.setBackgroundColor(normalBackground);

                switch (view.getId()) {
                    case R.id.draw:
                        fixtureGuessStatus = Config.FixtureGuessStatus.DRAW;
                        break;
                    case R.id.team1Win:
                        fixtureGuessStatus = Config.FixtureGuessStatus.TEAM1WIN;
                        break;
                    case R.id.team2Win:
                        fixtureGuessStatus = Config.FixtureGuessStatus.TEAM2WIN;
                        break;
                }

                ((Button) view).setTextColor(selectedTextColor);
                ((Button) view).setBackgroundColor(selectedBackground);

            }
        };

        draw.setOnClickListener(onSelectGuessFixtureStatusPressed);
        team1Win.setOnClickListener(onSelectGuessFixtureStatusPressed);
        team2Win.setOnClickListener(onSelectGuessFixtureStatusPressed);


        yes = view.findViewById(R.id.yes);
        no = view.findViewById(R.id.no);

        View.OnClickListener onIncludeGoalsGuessStatusPressed = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.yes) {
                    if (App.getInstance().getUser().goldenPoints < 2) {
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("عذرا لا توجد لديك قطع ذهبية كافية")
                                .setContentText("مشاهدة إعلان لإضافة قطعه ذهبية؟")
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
                        return;
                    }
                }
                int selectedTextColor = Color.WHITE;
                int selectedBackground = ContextCompat.getColor(getActivity(), R.color.colorAccent);
                int normalTextColor = Color.BLACK;
                int normalBackground = Color.TRANSPARENT;

                yes.setTextColor(normalTextColor);
                yes.setBackgroundColor(normalBackground);

                no.setTextColor(normalTextColor);
                no.setBackgroundColor(normalBackground);

                switch (view.getId()) {
                    case R.id.yes:
                        includeGoalsGuess = true;
                        break;
                    case R.id.no:
                        includeGoalsGuess = false;
                        break;
                }

                ((Button) view).setTextColor(selectedTextColor);
                ((Button) view).setBackgroundColor(selectedBackground);

            }
        };

        yes.setOnClickListener(onIncludeGoalsGuessStatusPressed);
        no.setOnClickListener(onIncludeGoalsGuessStatusPressed);

        view.findViewById(R.id.addGuess).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGuess();
            }
        });

        loadInfo();
        return view;
    }

    private void loadInfo() {
        team1.setText(fixture.team1);
        team2.setText(fixture.team2);
        team1Name.setText(fixture.team1);
        team2Name.setText(fixture.team2);
        team1Factor.setText(fixture.team1Factor + "x");
        team2Factor.setText(fixture.team2Factor + "x");
        goalsFactor.setText(fixture.goalsFactor + "x");
        draw.setText(draw.getText() + " " + fixture.drawFactor + "x");

        try {
            Picasso.with(getActivity()).load(fixture.team1Picture).error(R.drawable.news_placeholder).placeholder(R.drawable.news_placeholder).into(team1Picture);
        } catch (Exception e) {
            team1Picture.setImageResource(R.drawable.news_placeholder);
        }

        try {
            Picasso.with(getActivity()).load(fixture.team2Picture).error(R.drawable.news_placeholder).placeholder(R.drawable.news_placeholder).into(team2Picture);
        } catch (Exception e) {
            team2Picture.setImageResource(R.drawable.news_placeholder);
        }
    }

    private void addGuess() {
        if (fixtureGuessStatus == null) {
            MyToasty.info(getActivity(), "من فضلك اختر توقع أولا");
            return;
        }

        dialogHelper.showLoadingDialog(getActivity(), "جاري اضافة التوقع");

        AddGuessRequest addGuessRequest = new AddGuessRequest();
        addGuessRequest.fixtureStatus = fixtureGuessStatus;
        if (includeGoalsGuess) {
            addGuessRequest.team1Goals = Integer.parseInt(team1Goals.getNumber());
            addGuessRequest.team2Goals = Integer.parseInt(team2Goals.getNumber());
        }
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
                        goBack();
                    }
                })
                .show();
    }

    public AddFixtureGuessFragment setFixture(Fixture fixture) {
        this.fixture = fixture;
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
            case MessageEvent.UPDATE_COINS:
                goldenPoints.setText(App.getInstance().getUser().goldenPoints + "");
                break;
        }
    }
}
