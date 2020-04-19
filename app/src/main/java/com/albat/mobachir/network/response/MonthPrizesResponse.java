package com.albat.mobachir.network.response;

import com.albat.mobachir.network.models.MonthPrizes;
import com.albat.mobachir.network.models.Prize;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class MonthPrizesResponse extends Response {
    @SerializedName("data")
    public MonthPrizes monthPrizes;
}
