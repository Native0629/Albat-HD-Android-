package com.albat.mobachir.network.models;

import android.content.Intent;

import com.albat.mobachir.VideoPlayerActivity;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class News implements Serializable {
    @SerializedName("Id")
    public int id;

    @SerializedName("Title")
    public String title;

    @SerializedName("Content")
    public String content;

    @SerializedName("Source")
    public String source;

    @SerializedName("PubDate")
    public String pubDate;

    @SerializedName("CommentsCount")
    public int commentsCount;

    @SerializedName("Picture")
    public String picture;

    @SerializedName("Video")
    public String video;

    @SerializedName("Likes")
    public int likes;

    @SerializedName("LikedByMe")
    public Boolean likedByMe = false;
}
