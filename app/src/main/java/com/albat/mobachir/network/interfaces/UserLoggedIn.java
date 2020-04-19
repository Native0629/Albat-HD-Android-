package com.albat.mobachir.network.interfaces;

import com.albat.mobachir.network.models.User;

public interface UserLoggedIn {
    void onUserLoggedIn(User user, boolean success, String error);
}
