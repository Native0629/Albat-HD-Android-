package com.albat.mobachir.network.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class Lineup implements Serializable {
    @SerializedName("Id")
    public int id;

    @SerializedName("Team")
    public int team;

    @SerializedName("PlayerName")
    public String playerName;
}
