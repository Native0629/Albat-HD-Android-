package com.albat.mobachir.network.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Comment implements Serializable {
    @SerializedName("Id")
    public int id;

    @SerializedName("NewsId")
    public int newsId;

    @SerializedName("Author")
    public String author;

    @SerializedName("Content")
    public String content;

    @SerializedName("Date")
    public String pubDate;
}
