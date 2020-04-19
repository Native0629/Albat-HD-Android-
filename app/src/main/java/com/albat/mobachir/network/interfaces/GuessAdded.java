package com.albat.mobachir.network.interfaces;

import com.albat.mobachir.network.models.Guess;
import com.albat.mobachir.network.models.User;

public interface GuessAdded {
    void onGuessAdded(User user, Guess guess, boolean success, String error);

}
