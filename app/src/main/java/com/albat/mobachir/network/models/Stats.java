package com.albat.mobachir.network.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Stats implements Serializable {
    @SerializedName("Id")
    public int id;

    @SerializedName("FixtureId")
    public int fixtureId;

    @SerializedName("Minute")
    public int minute;

    @SerializedName("T1TotalShots")
    public int t1TotalShots;

    @SerializedName("T1OnGoalShots")
    public int t1OnGoalShots;

    @SerializedName("T1OffGoalShots")
    public int t1OffGoalShots;

    @SerializedName("T1Fouls")
    public int t1Fouls;

    @SerializedName("T1Corners")
    public int t1Corners;

    @SerializedName("T1Offsides")
    public int t1Offsides;

    @SerializedName("T1YellowCards")
    public int t1YellowCards;

    @SerializedName("T1RedCards")
    public int t1RedCards;

    @SerializedName("T1Substitutions")
    public int t1Substitutions;

    @SerializedName("T1Saves")
    public int t1Saves;

    @SerializedName("T1Possession")
    public int t1Possession;

    @SerializedName("T1FreeKicks")
    public int t1FreeKicks;

    @SerializedName("T2TotalShots")
    public int t2TotalShots;

    @SerializedName("T2OnGoalShots")
    public int t2OnGoalShots;

    @SerializedName("T2OffGoalShots")
    public int t2OffGoalShots;

    @SerializedName("T2Fouls")
    public int t2Fouls;

    @SerializedName("T2Corners")
    public int t2Corners;

    @SerializedName("T2Offsides")
    public int t2Offsides;

    @SerializedName("T2YellowCards")
    public int t2YellowCards;

    @SerializedName("T2RedCards")
    public int t2RedCards;

    @SerializedName("T2Substitutions")
    public int t2Substitutions;

    @SerializedName("T2Saves")
    public int t2Saves;

    @SerializedName("T2Possession")
    public int t2Possession;

    @SerializedName("T2FreeKicks")
    public int t2FreeKicks;
}
