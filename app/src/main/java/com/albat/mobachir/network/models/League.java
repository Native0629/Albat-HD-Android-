package com.albat.mobachir.network.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class League implements Serializable {
    @SerializedName("Id")
    public int id;

    @SerializedName("CountryId")
    public int countryId;

    @SerializedName("ArName")
    public String arName;

    @SerializedName("EnName")
    public String enName;

    @SerializedName("Picture")
    public String picture;

    @SerializedName("LiveStandings")
    public boolean liveStandings;

    @SerializedName("TopScorer")
    public boolean topScorer;

    @SerializedName("ActiveSeasonId")
    public int activeSeasonId;

    @SerializedName("ActiveRecordId")
    public int activeRecordId;

    @SerializedName("ActiveStageId")
    public int activeStageId;

    @SerializedName("Category")
    public int category;

    @SerializedName("Fixtures")
    public ArrayList<Fixture> fixtures = new ArrayList<>();

    @SerializedName("RSS")
    public String rss;
}
