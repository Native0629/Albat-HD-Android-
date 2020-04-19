package com.albat.mobachir.network.interfaces;

import com.albat.mobachir.network.models.Fixture;
import com.albat.mobachir.network.models.Guess;
import com.albat.mobachir.network.models.User;

public interface FixtureRetrieved {
    void onFixtureRetrieved(Fixture fixture, boolean success, String error);

}
