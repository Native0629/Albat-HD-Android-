package com.albat.mobachir.providers.guess;

import android.content.Context;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.albat.mobachir.App;
import com.albat.mobachir.Config;
import com.albat.mobachir.R;
import com.albat.mobachir.network.models.Fixture;
import com.albat.mobachir.network.models.MessageEvent;
import com.albat.mobachir.providers.guess.ui.GuessResultDialog;
import com.albat.mobachir.providers.guess.ui.GuessWinnerDialog;
import com.albat.mobachir.util.CalendarHelper;
import com.albat.mobachir.util.InfiniteRecyclerViewAdapter;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddGuessAdapter extends InfiniteRecyclerViewAdapter {
    private ArrayList<Fixture> data;
    private Context context;
    private FragmentManager fragmentManager;

    public AddGuessAdapter(ArrayList<Fixture> data, Context context, FragmentManager fragmentManager) {
        super(context, null);
        this.data = data;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }


    @Override
    protected int getViewType(int position) {
        return 0;
    }

    @Override
    protected RecyclerView.ViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        return new CViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_add_guess_item, parent, false));
    }

    @Override
    protected void doBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Fixture item = data.get(position);
        CViewHolder viewHolder = (CViewHolder) holder;
        viewHolder.team1.setText(item.team1);
        viewHolder.team2.setText(item.team2);
        viewHolder.date.setText(CalendarHelper.reformatDateString(item.dateTime, Config.API_DATETIME_FORMAT, Config.APP_DATE_FORMAT, item.timezone));
        viewHolder.time.setText(CalendarHelper.reformatDateString(item.dateTime, Config.API_DATETIME_FORMAT, Config.APP_TIME_FORMAT, item.timezone));
        viewHolder.guessResultMinCoins.setText(item.minResultCoins + "");
        viewHolder.guessWinnerMinCoins.setText(item.minWinnerCoins + "");

        try {
            Picasso.with(context).load(item.team1Picture).error(R.drawable.ic_team_no_image).placeholder(R.drawable.ic_team_no_image).into(viewHolder.team1Picture);
        } catch (Exception e) {
            viewHolder.team1Picture.setImageResource(R.drawable.ic_team_no_image);
        }

        try {
            Picasso.with(context).load(item.team2Picture).error(R.drawable.ic_team_no_image).placeholder(R.drawable.ic_team_no_image).into(viewHolder.team2Picture);
        } catch (Exception e) {
            viewHolder.team2Picture.setImageResource(R.drawable.ic_team_no_image);
        }

        if (!item.guessWinnerAvailable) {
            viewHolder.guessWinner.setEnabled(false);
            viewHolder.guessWinner.setAlpha(0.3f);
        } else {
            viewHolder.guessWinner.setEnabled(true);
            viewHolder.guessWinner.setAlpha(1f);
        }
        viewHolder.guessWinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (App.getInstance().getUser().goldenPoints < item.minWinnerCoins) {
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
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
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    Fragment prev = fragmentManager.findFragmentByTag("guessWinnerDialog");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);
                    DialogFragment dialogFragment = new GuessWinnerDialog().setFixture(item);
                    dialogFragment.show(ft, "guessWinnerDialog");
                }
            }
        });

        if (!item.guessResultAvailable) {
            viewHolder.guessResult.setEnabled(false);
            viewHolder.guessResult.setAlpha(0.3f);
        } else {
            viewHolder.guessResult.setEnabled(true);
            viewHolder.guessResult.setAlpha(1f);
        }
        viewHolder.guessResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (App.getInstance().getUser().goldenPoints < item.minResultCoins) {
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
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
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    Fragment prev = fragmentManager.findFragmentByTag("guessResultDialog");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);
                    DialogFragment dialogFragment = new GuessResultDialog().setFixture(item);
                    dialogFragment.show(ft, "guessResultDialog");
                }
            }
        });
    }

    @Override
    protected int getCount() {
        return data.size();
    }

    private class CViewHolder extends RecyclerView.ViewHolder {
        public TextView team1;
        public TextView team2;
        public TextView date;
        public TextView time;
        public CircleImageView team1Picture;
        public CircleImageView team2Picture;
        public LinearLayout guessResult;
        public LinearLayout guessWinner;
        public TextView guessResultMinCoins;
        public TextView guessWinnerMinCoins;

        public View itemView;

        private CViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            team1 = itemView.findViewById(R.id.team1);
            team2 = itemView.findViewById(R.id.team2);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            team1Picture = itemView.findViewById(R.id.team1Picture);
            team2Picture = itemView.findViewById(R.id.team2Picture);
            guessResult = itemView.findViewById(R.id.guessResult);
            guessWinner = itemView.findViewById(R.id.guessWinner);
            guessResultMinCoins = itemView.findViewById(R.id.guessResultMinCoins);
            guessWinnerMinCoins = itemView.findViewById(R.id.guessWinnerMinCoins);

        }
    }

    public void clear() {
        data.clear();
    }
}