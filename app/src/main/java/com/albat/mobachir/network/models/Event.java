package com.albat.mobachir.network.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Event implements Serializable {
    @SerializedName("Id")
    public String id;

    @SerializedName("Description")
    public String description;

    @SerializedName("Team")
    public int team;

    @SerializedName("Minute")
    public int minute;

    @SerializedName("Type")
    public int type;

    @SerializedName("Meta1")
    public String meta1;

    @SerializedName("Meta2")
    public String meta2;
}
