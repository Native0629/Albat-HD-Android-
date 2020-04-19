package com.albat.mobachir.network.interfaces;

import com.albat.mobachir.network.models.News;
import com.albat.mobachir.network.models.Stage;

import java.util.ArrayList;

public interface StagesRetrieved {
    void onStagesRetrieved(ArrayList<Stage> stages, boolean success, String error);
}
