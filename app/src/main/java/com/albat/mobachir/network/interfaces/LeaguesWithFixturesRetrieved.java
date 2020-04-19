package com.albat.mobachir.network.interfaces;

import com.albat.mobachir.network.models.League;
import com.albat.mobachir.network.models.News;

import java.util.ArrayList;

public interface LeaguesWithFixturesRetrieved {
    void onLeaguesWithFixturesRetrieved(ArrayList<League> leagues, boolean success, String error);
}
