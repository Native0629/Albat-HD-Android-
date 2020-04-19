package com.albat.mobachir.network.response;

import com.albat.mobachir.network.models.Comment;
import com.albat.mobachir.network.models.Guess;
import com.albat.mobachir.network.models.User;
import com.google.gson.annotations.SerializedName;


public class AddGuessResponse extends Response {
    @SerializedName("data")
    public AddGuessResponseData addGuessResponseData;


    public class AddGuessResponseData {
        @SerializedName("user")
        public User user;

        @SerializedName("guess")
        public Guess guess;
    }
}
