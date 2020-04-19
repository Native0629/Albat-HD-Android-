package com.albat.mobachir.network.response;

import com.albat.mobachir.network.models.Fixture;
import com.albat.mobachir.network.models.League;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class FixtureResponse extends Response {
    @SerializedName("data")
    public Fixture fixture;
}
