package com.albat.mobachir.network.response;

import com.albat.mobachir.network.models.News;
import com.albat.mobachir.network.models.User;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class NewsResponse extends Response {
    @SerializedName("data")
    public ArrayList<News> news;
}
