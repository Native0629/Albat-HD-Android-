package com.albat.mobachir.network.response;

import com.albat.mobachir.network.models.User;
import com.google.gson.annotations.SerializedName;


public class UserResponse extends Response {
    @SerializedName("data")
    public User user;
}
