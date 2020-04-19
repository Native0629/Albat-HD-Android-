package com.albat.mobachir.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FixtureLink implements Serializable {
    @Expose
    @SerializedName("Link")
    public String link;

    @Expose
    @SerializedName("LinkName")
    public String linkName;
}
