package com.albat.mobachir.network.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class User implements Serializable {
    @SerializedName("Id")
    public int id;

    @SerializedName("LoginType")
    public int loginType;

    @SerializedName("LoginId")
    public String loginId;

    @SerializedName("Email")
    public String email;

    @SerializedName("Name")
    public String name;

    @SerializedName("GoldenPoints")
    public int goldenPoints;

    @SerializedName("Points")
    public int points;
}
