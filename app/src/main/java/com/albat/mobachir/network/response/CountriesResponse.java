package com.albat.mobachir.network.response;

import com.albat.mobachir.network.models.Country;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class CountriesResponse extends Response {
    @SerializedName("data")
    public ArrayList<Country> countries;
}
