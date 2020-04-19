package com.albat.mobachir.network.response;

import com.albat.mobachir.network.models.League;
import com.albat.mobachir.network.models.TopScorer;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class TopScorersResponse extends Response {
    @SerializedName("data")
    public ArrayList<TopScorer> topScorers;
}
