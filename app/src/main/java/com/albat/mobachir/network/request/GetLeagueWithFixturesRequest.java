package com.albat.mobachir.network.request;


import java.util.ArrayList;

public class GetLeagueWithFixturesRequest {
    public int mode;
    public String date;
    public int userId;
    public ArrayList<Integer> leaguesIds;
}