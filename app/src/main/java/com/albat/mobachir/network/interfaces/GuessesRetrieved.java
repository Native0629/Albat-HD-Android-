package com.albat.mobachir.network.interfaces;


import com.albat.mobachir.network.models.Guess;
import com.albat.mobachir.network.models.League;

import java.util.ArrayList;

public interface GuessesRetrieved {
    void onGuessesRetrieved(ArrayList<Guess> guesses, boolean success, String error);
}
