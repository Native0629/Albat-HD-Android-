package com.albat.mobachir.network.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class TopScorer implements Serializable {
    @SerializedName("Id")
    public int id;

    @SerializedName("SeasonId")
    public int seasonId;

    @SerializedName("ArName")
    public String arName;

    @SerializedName("EnName")
    public String enName;

    @SerializedName("Team")
    public String team;

    @SerializedName("PlayerPicture")
    public String playerPicture;

    @SerializedName("Goals")
    public int goals;

    @SerializedName("Rank")
    public int rank;
}
