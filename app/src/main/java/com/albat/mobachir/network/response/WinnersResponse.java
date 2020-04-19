package com.albat.mobachir.network.response;

import com.albat.mobachir.network.models.Guess;
import com.albat.mobachir.network.models.Winner;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class WinnersResponse extends Response {
    @SerializedName("data")
    public ArrayList<Winner> winners;
}
