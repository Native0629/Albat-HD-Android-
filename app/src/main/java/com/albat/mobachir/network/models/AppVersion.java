package com.albat.mobachir.network.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class AppVersion implements Serializable {
    @SerializedName("AppVersion")
    public String appVersion;

    @SerializedName("AppUrl")
    public String appUrl;
}
