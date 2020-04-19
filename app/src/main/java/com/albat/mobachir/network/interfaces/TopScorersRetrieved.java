package com.albat.mobachir.network.interfaces;


import com.albat.mobachir.network.models.League;
import com.albat.mobachir.network.models.TopScorer;

import java.util.ArrayList;

public interface TopScorersRetrieved {
    void onTopScorersRetrieved(ArrayList<TopScorer> topScorers, boolean success, String error);
}
