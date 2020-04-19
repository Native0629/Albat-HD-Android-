package com.albat.mobachir.network.interfaces;


import com.albat.mobachir.network.models.Fixture;
import com.albat.mobachir.network.models.League;

import java.util.ArrayList;

public interface FixturesRetrieved {
    void onFixturesRetrieved(ArrayList<Fixture> fixtures, boolean success, String error);
}
