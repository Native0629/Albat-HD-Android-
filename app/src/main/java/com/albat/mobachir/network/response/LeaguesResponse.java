package com.albat.mobachir.network.response;

import com.albat.mobachir.network.models.Country;
import com.albat.mobachir.network.models.League;
import com.albat.mobachir.network.models.LeaguesData;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class LeaguesResponse extends Response {
    @SerializedName("data")
    public LeaguesData leaguesData;
}
