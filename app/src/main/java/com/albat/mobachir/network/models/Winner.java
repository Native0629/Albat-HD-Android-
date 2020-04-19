package com.albat.mobachir.network.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.Month;
import java.time.Year;


public class Winner implements Serializable {
    @SerializedName("Id")
    public int id;

    @SerializedName("UserId")
    public int userId;

    @SerializedName("Rank")
    public int rank;

    @SerializedName("Points")
    public int points;

    @SerializedName("Month")
    public int month;

    @SerializedName("Year")
    public int year;

    @SerializedName("Prize")
    public Prize prize;
}
