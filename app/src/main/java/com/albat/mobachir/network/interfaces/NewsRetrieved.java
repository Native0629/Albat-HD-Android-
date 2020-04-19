package com.albat.mobachir.network.interfaces;

import com.albat.mobachir.network.models.News;

import java.util.ArrayList;

public interface NewsRetrieved {
    void onNewsRetrieved(ArrayList<News> news, boolean success, String error);
}
