package com.albat.mobachir.network.response;

import com.albat.mobachir.network.models.League;
import com.albat.mobachir.network.models.LeaguesData;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class LeaguesWithFixturesResponse extends Response {
    @SerializedName("data")
    public ArrayList<League> leagues;
}
