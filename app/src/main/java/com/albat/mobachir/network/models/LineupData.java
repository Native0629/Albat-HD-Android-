package com.albat.mobachir.network.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class LineupData implements Serializable{
    @SerializedName("Team1")
    public ArrayList<Lineup> team1;

    @SerializedName("Team2")
    public ArrayList<Lineup> team2;
}
