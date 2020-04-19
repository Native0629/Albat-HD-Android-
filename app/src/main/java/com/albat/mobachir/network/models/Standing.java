package com.albat.mobachir.network.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Standing implements Serializable {
    @SerializedName("Id")
    public int id;

    @SerializedName("StageId")
    public int stageId;

    @SerializedName("SeasonId")
    public int seasonId;

    @SerializedName("Rank")
    public int rank;

    @SerializedName("ArTeam")
    public String arTeam;

    @SerializedName("EnTeam")
    public String enTeam;

    @SerializedName("TeamPicture")
    public String teamPicture;

    @SerializedName("Win")
    public int win;

    @SerializedName("Draw")
    public int draw;

    @SerializedName("Lose")
    public int lose;

    @SerializedName("Points")
    public int points;

    @SerializedName("GamesPlayed")
    public int gamesPlayed;

    @SerializedName("GoalsScored")
    public int goalsScored;

    @SerializedName("GoalsAgainst")
    public int goalsAgainst;

    @SerializedName("GroupId")
    public Integer groupId;

    @SerializedName("GroupName")
    public String groupName;
}