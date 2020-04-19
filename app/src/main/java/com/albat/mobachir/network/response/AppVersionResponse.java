package com.albat.mobachir.network.response;

import com.albat.mobachir.network.models.AppVersion;
import com.albat.mobachir.network.models.Guess;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class AppVersionResponse extends Response {
    @SerializedName("data")
    public AppVersion appVersion;
}
