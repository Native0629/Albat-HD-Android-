package com.albat.mobachir.network.interfaces;


import com.albat.mobachir.network.models.Leaderboard;

import java.util.ArrayList;

public interface LeaderboardRetrieved {
    void onLeaderboardRetrieved(ArrayList<Leaderboard> leaderboard, boolean success, String error);
}
