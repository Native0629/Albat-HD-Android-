package com.albat.mobachir.network.response;

import com.google.gson.annotations.SerializedName;


public class Response {
    @SerializedName("code")
    public int code;
    @SerializedName("error")
    public String error;
    @SerializedName("message")
    public String message;
}
