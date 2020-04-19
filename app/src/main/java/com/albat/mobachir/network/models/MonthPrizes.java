package com.albat.mobachir.network.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class MonthPrizes implements Serializable {
    @SerializedName("Id")
    public int id;

    @SerializedName("Month")
    public int month;

    @SerializedName("Year")
    public int year;

    @SerializedName("Prize1")
    public Prize prize1;

    @SerializedName("Prize2")
    public Prize prize2;

    @SerializedName("Prize3")
    public Prize prize3;
}
