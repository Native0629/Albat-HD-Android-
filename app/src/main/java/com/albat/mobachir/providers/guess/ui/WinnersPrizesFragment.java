package com.albat.mobachir.providers.guess.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.albat.mobachir.BaseFragment;
import com.albat.mobachir.R;
import com.albat.mobachir.network.interfaces.MonthPrizesRetrieved;
import com.albat.mobachir.network.managers.UserManager;
import com.albat.mobachir.network.models.MessageEvent;
import com.albat.mobachir.network.models.MonthPrizes;
import com.albat.mobachir.network.models.Prize;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;

public class WinnersPrizesFragment extends BaseFragment implements MonthPrizesRetrieved {
    String TAG = "WinnersPrizesFragment";
    //Views
    private final String[] MONTHS = new String[]{"يناير", "فبراير", "مارس", "إبريل", "مايو", "يونيو", "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر"};

    Spinner monthSpinner;
    ArrayAdapter<String> monthsAdapter;

    LinearLayout prize1Layout, prize2Layout, prize3Layout;
    ImageView prize1Picture, prize2Picture, prize3Picture;
    TextView prize1Name, prize2Name, prize3Name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_winners_prizes, null);
        setHasOptionsMenu(true);

        prize1Layout = view.findViewById(R.id.prize1Layout);
        prize2Layout = view.findViewById(R.id.prize2Layout);
        prize3Layout = view.findViewById(R.id.prize3Layout);

        prize1Picture = view.findViewById(R.id.prize1Picture);
        prize2Picture = view.findViewById(R.id.prize2Picture);
        prize3Picture = view.findViewById(R.id.prize3Picture);

        prize1Name = view.findViewById(R.id.prize1Name);
        prize2Name = view.findViewById(R.id.prize2Name);
        prize3Name = view.findViewById(R.id.prize3Name);


        monthSpinner = view.findViewById(R.id.monthsSpinner);
        monthsAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_layout);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        for (int i = 0; i < MONTHS.length; i++) {
            if (i == currentMonth)
                monthsAdapter.add("الشهر الحالي");
            else
                monthsAdapter.add(MONTHS[i]);
        }
        monthsAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthsAdapter);
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                loadItems(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        monthSpinner.setSelection(currentMonth, true);


        return view;
    }

    int selectedMonth = 0;

    public void loadItems(int monthNumber) {
        selectedMonth = monthNumber;
        UserManager userManager = new UserManager();
        userManager.getMonthPrizes(monthNumber, this);
    }

    @Override
    public void onMonthPrizesRetrieved(MonthPrizes monthPrizes, boolean success, String error) {
        if (!success || monthPrizes == null) {
            prize1Layout.setVisibility(View.GONE);
            prize2Layout.setVisibility(View.GONE);
            prize3Layout.setVisibility(View.GONE);
            return;
        } else {
            loadPrizeInfo(monthPrizes.prize1, prize1Name, prize1Picture, prize1Layout);
            loadPrizeInfo(monthPrizes.prize2, prize2Name, prize2Picture, prize2Layout);
            loadPrizeInfo(monthPrizes.prize3, prize3Name, prize3Picture, prize3Layout);
        }
    }


    private void loadPrizeInfo(final Prize prize, TextView prizeName, ImageView prizePicture, LinearLayout prizeLayout) {
        String notDeterminedYet = "لم تحدد بعد";
        if (prize != null) {
            prizeLayout.setVisibility(View.VISIBLE);
            prizeName.setText(prize.name);
            try {
                Picasso.with(getActivity()).load(prize.picture).error(R.drawable.ic_gift).placeholder(R.drawable.ic_gift).into(prizePicture);
            } catch (Exception e) {
                prizePicture.setImageResource(R.drawable.ic_gift);
            }
            prizeLayout.setTag(prize);
            prizeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = new PrizeDetailsFragment().setPrize(prize);
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.add(R.id.container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commitAllowingStateLoss();
                }
            });
        } else {
            prizeLayout.setVisibility(View.GONE);
            prizeName.setText(notDeterminedYet);
            prizePicture.setImageResource(R.drawable.ic_gift);
        }
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
                loadItems(selectedMonth);
                break;
        }
    }
}
