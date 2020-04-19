package com.albat.mobachir.network.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Leaderboard implements Serializable {
    @SerializedName("Rank")
    public int rank;

    @SerializedName("Picture")
    public String picture;

    @SerializedName("Name")
    public String name;

    @SerializedName("Points")
    public int points;

    @SerializedName("Id")
    public int id;
}
