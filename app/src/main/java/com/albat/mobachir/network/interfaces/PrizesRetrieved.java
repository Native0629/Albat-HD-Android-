package com.albat.mobachir.network.interfaces;


import com.albat.mobachir.network.models.Prize;

import java.util.ArrayList;

public interface PrizesRetrieved {
    void onPrizesRetrieved(ArrayList<Prize> prizes, boolean success, String error);
}
