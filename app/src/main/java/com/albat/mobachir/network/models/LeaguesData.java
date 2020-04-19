package com.albat.mobachir.network.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LeaguesData {
    @SerializedName("ArabicLeagues")
    public ArrayList<League> arabicLeagues;

    @SerializedName("WorldLeagues")
    public ArrayList<League> worldLeagues;

    @SerializedName("AfricanLeagues")
    public ArrayList<League> africanLeagues;

    @SerializedName("EuroLeagues")
    public ArrayList<League> euroLeagues;

    @SerializedName("AsianLeagues")
    public ArrayList<League> asianLeagues;

    @SerializedName("ImportantLeagues")
    public ArrayList<League> importantLeagues;
}
