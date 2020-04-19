package com.albat.mobachir.network.interfaces;


import com.albat.mobachir.network.models.MonthPrizes;
import com.albat.mobachir.network.models.Winner;

import java.util.ArrayList;

public interface MonthPrizesRetrieved {
    void onMonthPrizesRetrieved(MonthPrizes winners, boolean success, String error);
}
