package com.albat.mobachir.network.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Country implements Serializable {
    @SerializedName("Id")
    public int id;

    @SerializedName("ArName")
    public String arName;

    @SerializedName("EnName")
    public String enName;
}
