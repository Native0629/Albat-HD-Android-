package com.albat.mobachir.network.interfaces;


import com.albat.mobachir.network.models.Guess;
import com.albat.mobachir.network.models.Winner;

import java.util.ArrayList;

public interface WinnersRetrieved {
    void onWinnersRetrieved(ArrayList<Winner> winners, boolean success, String error);
}
