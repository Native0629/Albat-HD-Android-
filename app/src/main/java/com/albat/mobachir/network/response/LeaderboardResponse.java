package com.albat.mobachir.network.response;

import com.albat.mobachir.network.models.Guess;
import com.albat.mobachir.network.models.Leaderboard;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class LeaderboardResponse extends Response {
    @SerializedName("data")
    public ArrayList<Leaderboard> leaderboard;
}
