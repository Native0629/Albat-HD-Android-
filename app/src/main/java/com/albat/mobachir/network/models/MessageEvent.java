package com.albat.mobachir.network.models;


public class MessageEvent {
    public static final int WATCH_ADD = 0;
    public static final int UPDATE_COINS = 1;
    public static final int SHOW_INTERSTITIAL_AD = 2;
    public static final int REFRESH = 3;

    int code;

    public MessageEvent(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
