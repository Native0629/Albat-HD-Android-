package com.albat.mobachir.network.response;

import com.albat.mobachir.network.models.Comment;
import com.albat.mobachir.network.models.News;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class CommentsResponse extends Response {
    @SerializedName("data")
    public ArrayList<Comment> comments;
}
