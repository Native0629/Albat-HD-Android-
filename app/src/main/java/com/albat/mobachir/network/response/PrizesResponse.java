package com.albat.mobachir.network.response;

import com.albat.mobachir.network.models.Prize;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class PrizesResponse extends Response {
    @SerializedName("data")
    public ArrayList<Prize> prizes;
}
