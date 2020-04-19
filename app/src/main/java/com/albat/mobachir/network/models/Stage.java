package com.albat.mobachir.network.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class Stage implements Serializable {
    @SerializedName("Id")
    public int id;

    @SerializedName("SeasonId")
    public int seasonId;

    @SerializedName("ArName")
    public String arName;

    @SerializedName("EnName")
    public String enName;

    @SerializedName("Standings")
    public ArrayList<Standing> standings = new ArrayList<>();

}
