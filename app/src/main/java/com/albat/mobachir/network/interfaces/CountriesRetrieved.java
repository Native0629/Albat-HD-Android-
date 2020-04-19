package com.albat.mobachir.network.interfaces;


import com.albat.mobachir.network.models.Country;

import java.util.ArrayList;

public interface CountriesRetrieved {
    void onCountriesRetrieved(ArrayList<Country> countries, boolean success, String error);
}
