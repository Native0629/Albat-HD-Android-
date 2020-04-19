package com.albat.mobachir.network.interfaces;


public interface NewLikeToggled {
    void onNewLikeToggled(boolean likedByMe, int likes, boolean success, String error);
}
