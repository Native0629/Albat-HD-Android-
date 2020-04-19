package com.albat.mobachir.providers.worldcup;

import android.app.Activity;
import android.os.AsyncTask;

import com.albat.mobachir.util.Helper;
import com.albat.mobachir.util.Log;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This file is part of the Universal template
 * For license information, please check the LICENSE
 * file in the root of this project
 *
 * @author Sherdle
 * Copyright 2017
 */

/**
 * Async task class to get json by making HTTP call
 */
public class WorldCupParser extends AsyncTask<Void, Void, Void> {
    String TAG = "WorldCupParser";
    //Instance variables
    private String sourceLocation;
    private Activity context;
    private CallBack callback;

    private ArrayList<MatchItem> result;

    private boolean facedException;

    public WorldCupParser(String sourceLocation, Activity context, CallBack callback) {
        this.sourceLocation = sourceLocation;
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... args) {

        JSONArray jsonMatches = null;

        try {
            //Get the JSON
            if (sourceLocation.contains("http")) {
                String jsonStr = Helper.getDataFromUrl(sourceLocation);
                jsonMatches = new JSONArray(jsonStr);
            }
        } catch (Exception e) {
            Log.e("INFO", "JSON was invalid");
            facedException = true;
            e.printStackTrace();
        }


        if (jsonMatches != null) {

            result = new ArrayList<MatchItem>();
            Gson gson = new Gson();

            try {
                // looping through all menu items
                for (int i = 0; i < jsonMatches.length(); i++) {
                    try {
                        JSONObject matchObject = jsonMatches.getJSONObject(i);
                        MatchItem matchItem = gson.fromJson(matchObject.toString(), MatchItem.class);

//                        ArrayList links = new ArrayList();
//                        if (matchObject.has("Links")) {
//                            JSONArray jsonLinks = matchObject.getJSONArray("Links");
//                            for (int j = 0; j < jsonLinks.length(); j++)
//                                links.add(jsonLinks.getString(j));
//                        }
//                        matchItem.setLinks(links);
                        result.add(matchItem);
                    } catch (Exception e) {
                        Log.e("INFO", "JSON was invalid");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("INFO", "JSON was invalid");
                facedException = true;
            }

        } else {
            Log.e("INFO", "JSON Could not be retrieved");
            facedException = true;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void args) {
        if (callback != null)
            callback.matchesLoaded(result, facedException);
    }

    public interface CallBack {
        void matchesLoaded(ArrayList<MatchItem> result, boolean failed);
    }


}
