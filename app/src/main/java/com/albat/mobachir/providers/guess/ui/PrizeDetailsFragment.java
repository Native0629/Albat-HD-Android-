package com.albat.mobachir.providers.guess.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.albat.mobachir.BaseFragment;
import com.albat.mobachir.R;
import com.albat.mobachir.network.models.Prize;
import com.squareup.picasso.Picasso;

public class PrizeDetailsFragment extends BaseFragment {
    String TAG = "PrizeDetailsFragment";
    //Views


    ImageView picture;
    TextView name, description;

    Prize prize;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_prize_details, null);
        setHasOptionsMenu(true);

        picture = view.findViewById(R.id.picture);
        name = view.findViewById(R.id.name);
        description = view.findViewById(R.id.description);

        name.setText(prize.name);
        description.setText(prize.description);

        try {
            Picasso.with(getActivity()).load(prize.picture).error(R.drawable.ic_gift).placeholder(R.drawable.ic_gift).into(picture);
        } catch (Exception e) {
            picture.setImageResource(R.drawable.ic_gift);
        }

        return view;
    }

    public PrizeDetailsFragment setPrize(Prize prize) {
        this.prize = prize;
        return this;
    }
}
