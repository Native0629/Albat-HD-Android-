package com.albat.mobachir.network.interfaces;

import com.albat.mobachir.network.models.User;

public interface UserUpdated {
    void onUserUpdated(User user, boolean success, String error);
}
