package com.albat.mobachir.network.interfaces;


import com.albat.mobachir.network.models.Country;
import com.albat.mobachir.network.models.League;
import com.albat.mobachir.network.models.LeaguesData;

import java.util.ArrayList;

public interface LeaguesRetrieved {
    void onLeaguesRetrieved(LeaguesData leaguesData, boolean success, String error);
}
