package com.albat.mobachir.network.response;

import com.albat.mobachir.network.models.Stage;
import com.albat.mobachir.network.models.TopScorer;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class StagesResponse extends Response {
    @SerializedName("data")
    public ArrayList<Stage> stages;
}
