package com.albat.mobachir.network.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Guess implements Serializable {
    @SerializedName("Id")
    public int id;

    @SerializedName("FixtureId")
    public int fixtureId;

    @SerializedName("Manual")
    public boolean manual;

    @SerializedName("UserId")
    public int userId;

    @SerializedName("FixtureStatus")
    public int fixtureStatus;

    @SerializedName("Team1Goals")
    public Integer team1Goals;

    @SerializedName("Team2Goals")
    public Integer team2Goals;

    @SerializedName("ResultBid")
    public Integer resultBid;

    @SerializedName("WinnerBid")
    public Integer winnerBid;

    @SerializedName("DateTime")
    public String dateTime;

    @SerializedName("Resolved")
    public boolean resolved;

    @SerializedName("PointsReceived")
    public int pointsReceived;

    @SerializedName("League")
    public League league;

    @SerializedName("Fixture")
    public Fixture fixture;
}


