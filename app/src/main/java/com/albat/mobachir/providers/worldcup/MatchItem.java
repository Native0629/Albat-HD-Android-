package com.albat.mobachir.providers.worldcup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class MatchItem implements Serializable {

    @Expose
    @SerializedName("Id")
    int id;

    @Expose
    @SerializedName("Team1")
    TeamItem team1;

    @Expose
    @SerializedName("Team2")
    TeamItem team2;

    @Expose
    @SerializedName("Title")
    String title;

    @Expose
    @SerializedName("Commentator")
    String commentator;

    @Expose
    @SerializedName("Channel")
    String channel;

    @Expose
    @SerializedName("Stadium")
    String stadium;

    @Expose
    @SerializedName("Info")
    String info;

    @Expose
    @SerializedName("DateTime")
    String dateTime;

    @Expose
    @SerializedName("Notes")
    String notes;

    @Expose
    @SerializedName("Manual")
    boolean manual = true;

    @Expose
    @SerializedName("Timezone")
    String timezone;

    @Expose
    @SerializedName("Links")
    ArrayList<LinkItem> links = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TeamItem getTeam1() {
        return team1;
    }

    public void setTeam1(TeamItem team1) {
        this.team1 = team1;
    }

    public TeamItem getTeam2() {
        return team2;
    }

    public void setTeam2(TeamItem team2) {
        this.team2 = team2;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCommentator() {
        return commentator;
    }

    public void setCommentator(String commentator) {
        this.commentator = commentator;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ArrayList<LinkItem> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<LinkItem> links) {
        this.links = links;
    }

    public boolean isManual() {
        return manual;
    }

    public void setManual(boolean manual) {
        this.manual = manual;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
