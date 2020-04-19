package com.albat.mobachir.network.response;

import com.albat.mobachir.network.models.Guess;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class GuessesResponse extends Response {
    @SerializedName("data")
    public ArrayList<Guess> guesses;
}
