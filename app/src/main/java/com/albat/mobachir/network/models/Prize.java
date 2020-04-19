package com.albat.mobachir.network.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class Prize implements Serializable {
    @SerializedName("Id")
    public int id;

    @SerializedName("Picture")
    public String picture;

    @SerializedName("Name")
    public String name;

    @SerializedName("Description")
    public String description;
}
