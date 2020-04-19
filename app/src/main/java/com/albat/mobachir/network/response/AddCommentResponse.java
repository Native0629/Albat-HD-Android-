package com.albat.mobachir.network.response;

import com.albat.mobachir.network.models.Comment;
import com.albat.mobachir.network.models.User;
import com.google.gson.annotations.SerializedName;


public class AddCommentResponse extends Response {
    @SerializedName("data")
    public Comment comment;
}
