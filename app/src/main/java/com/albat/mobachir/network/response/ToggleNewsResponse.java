package com.albat.mobachir.network.response;

import com.albat.mobachir.network.models.Guess;
import com.albat.mobachir.network.models.User;
import com.google.gson.annotations.SerializedName;


public class ToggleNewsResponse extends Response {
    @SerializedName("data")
    public ToggleNewsResponseData toggleNewsResponseData;


    public class ToggleNewsResponseData {
        @SerializedName("LikedByMe")
        public Boolean likedByMe = false;

        @SerializedName("Likes")
        public Integer likes = 0;
    }
}
