package com.albat.mobachir.providers.worldcup;

import android.app.Activity;
import android.os.AsyncTask;

import com.albat.mobachir.util.Helper;
import com.albat.mobachir.util.Log;
import com.google.gson.Gson;

import org.json.JSONObject;

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
public class MatchParser extends AsyncTask<Void, Void, Void> {
    String TAG = "MatchParser";
    //Instance variables
    private String sourceLocation;
    private Activity context;
    private CallBack callback;

    private MatchItem result;

    private boolean facedException;

    public MatchParser(String sourceLocation, Activity context, CallBack callback) {
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

        JSONObject matchObject = null;

        try {
            //Get the JSON
            if (sourceLocation.contains("http")) {
                String jsonStr = Helper.getDataFromUrl(sourceLocation);
                matchObject = new JSONObject(jsonStr);
            }
        } catch (Exception e) {
            Log.e("INFO", "JSON was invalid");
            facedException = true;
            e.printStackTrace();
        }


        if (matchObject != null) {

            Gson gson = new Gson();

            try {
                result = gson.fromJson(matchObject.toString(), MatchItem.class);
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
            callback.matchLoaded(result, facedException);
    }

    public interface CallBack {
        void matchLoaded(MatchItem result, boolean failed);
    }


}
