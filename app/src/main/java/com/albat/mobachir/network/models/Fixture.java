package com.albat.mobachir.network.models;

import com.albat.mobachir.Config;
import com.albat.mobachir.util.CalendarHelper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;


public class Fixture implements Serializable {
    @SerializedName("Id")
    public int id;

    @SerializedName("Team1")
    public String team1;

    @SerializedName("Team1Picture")
    public String team1Picture;

    @SerializedName("Team1Goals")
    public int team1Goals;

    @SerializedName("Team1PenGoals")
    public int team1PenGoals;

    @SerializedName("Team2")
    public String team2;

    @SerializedName("Team2Picture")
    public String team2Picture;

    @SerializedName("Team2Goals")
    public int team2Goals;

    @SerializedName("Team2PenGoals")
    public int team2PenGoals;

    @SerializedName("DateTime")
    public String dateTime;

    @SerializedName("Timezone")
    public String timezone;

    @SerializedName("SeasonId")
    public int seasonId;

    @SerializedName("StageId")
    public int stageId;

    @SerializedName("Broadcast")
    public boolean broadcast;

    @SerializedName("Team1Factor")
    public int team1Factor;

    @SerializedName("Team2Factor")
    public int team2Factor;

    @SerializedName("DrawFactor")
    public int drawFactor;

    @SerializedName("GoalsFactor")
    public int goalsFactor;

    @SerializedName("MinWinnerCoins")
    public int minWinnerCoins;

    @SerializedName("MinResultCoins")
    public int minResultCoins;

    @SerializedName("Manual")
    public boolean manual;

    @SerializedName("Stadium")
    public String stadium;

    @SerializedName("Commentator")
    public String commentator;

    @SerializedName("Channel")
    public String channel;

    @SerializedName("Title")
    public String title;

    @SerializedName("League")
    public League league;

    @SerializedName("ResultAdded")
    public boolean resultAdded;

    @SerializedName("Status")
    public String status;

    @Expose
    @SerializedName("GuessWinnerAvailable")
    public Boolean guessWinnerAvailable;

    @SerializedName("GuessResultAvailable")
    public Boolean guessResultAvailable;

    @SerializedName("LineupData")
    public LineupData lineupData;

    @SerializedName("Stats")
    public Stats stats;

    @SerializedName("Events")
    public ArrayList<Event> events;

    @SerializedName("Links")
    public ArrayList<FixtureLink> links;

    public interface MatchStatus {
        String POSTPONED = "POSTP";
        String CANCELED = "CANCL";
        String DELAYED = "DELAYED";
        String INTERRUPTED = "INT";
        String SUSPENDED = "SUSP";
        String NOT_STARTED = "NS";
        String LIVE = "LIVE";
        String HALF_TIME = "HT";
        String BREAK = "BREAK";
        String EXTRA_TIME = "ET";
        String PENALITY_LIVE = "PEN_LIVE";
        String ENDED_AFTER_FULL_TIME = "FT";
        String ENDED_AFTER_EXTRA_TIME = "AET";
        String ENDED_AFTER_PENALITY_SHOOTOUT = "FT_PEN";
        String ENDED = "ENDED";
    }

    public String getStatus() {
        if (manual) {
            return getManualStatus();
        }

        if (status == null)
            return getManualStatus();

        switch (status) {
            case MatchStatus.NOT_STARTED:
                return MatchStatus.NOT_STARTED;
            case MatchStatus.LIVE:
                return MatchStatus.LIVE;
            case MatchStatus.HALF_TIME:
                return MatchStatus.HALF_TIME;
            case MatchStatus.BREAK:
                return MatchStatus.BREAK;
            case MatchStatus.EXTRA_TIME:
                return MatchStatus.EXTRA_TIME;
            case MatchStatus.PENALITY_LIVE:
                return MatchStatus.PENALITY_LIVE;
            case MatchStatus.ENDED_AFTER_FULL_TIME:
                return MatchStatus.ENDED_AFTER_FULL_TIME;
            case MatchStatus.ENDED_AFTER_EXTRA_TIME:
                return MatchStatus.ENDED_AFTER_EXTRA_TIME;
            case MatchStatus.ENDED_AFTER_PENALITY_SHOOTOUT:
                return MatchStatus.ENDED_AFTER_PENALITY_SHOOTOUT;
            case MatchStatus.POSTPONED:
                return MatchStatus.POSTPONED;
            case MatchStatus.DELAYED:
                return MatchStatus.DELAYED;
            case MatchStatus.CANCELED:
                return MatchStatus.CANCELED;
            case MatchStatus.SUSPENDED:
                return MatchStatus.SUSPENDED;
            case MatchStatus.INTERRUPTED:
                return MatchStatus.INTERRUPTED;
        }
        return MatchStatus.NOT_STARTED;
    }

    public String getStatusٍString() {
        String statusStr = "غير محدده";
        switch (getStatus()) {
            case MatchStatus.NOT_STARTED:
                statusStr = "لم تبدأ بعد";
                break;
            case MatchStatus.LIVE:
            case MatchStatus.HALF_TIME:
            case MatchStatus.BREAK:
            case MatchStatus.EXTRA_TIME:
            case MatchStatus.PENALITY_LIVE:
                statusStr = "جارية الآن";
                break;
            case MatchStatus.ENDED_AFTER_FULL_TIME:
            case MatchStatus.ENDED_AFTER_EXTRA_TIME:
            case MatchStatus.ENDED_AFTER_PENALITY_SHOOTOUT:
            case MatchStatus.ENDED:
                statusStr = "إنتهت";
                break;
            case MatchStatus.POSTPONED:
                statusStr = "مؤجلة";
                break;
            case MatchStatus.DELAYED:
                statusStr = "تم تأخيرها";
                break;
            case MatchStatus.CANCELED:
                statusStr = "تم إلغائها";
                break;
            case MatchStatus.SUSPENDED:
            case MatchStatus.INTERRUPTED:
                statusStr = "تم إيقافها";
                break;
        }
        return statusStr;
    }

    private String getManualStatus() {
        String matchDateInCurrentTimezone = CalendarHelper.reformatDateString(dateTime, Config.API_DATETIME_FORMAT, Config.API_DATETIME_FORMAT, timezone, false);
        Date matchDate = CalendarHelper.stringToDate(dateTime, Config.API_DATETIME_FORMAT, timezone);
        String currentDateStr = CalendarHelper.getCurrentDateFormatted(Config.API_DATETIME_FORMAT);
        if (matchDateInCurrentTimezone.compareTo(currentDateStr) > 0) {
            return MatchStatus.NOT_STARTED;
        }

        Date matchEndTime = new Date();
        matchEndTime.setTime(matchDate.getTime() + (long) (4.5 * 60 * 60 * 1000));
        String matchEndTimeStr = CalendarHelper.formatDate(matchEndTime, Config.API_DATETIME_FORMAT, false);
        if (currentDateStr.compareTo(matchEndTimeStr) > 0) {
            return MatchStatus.ENDED;
        }
        return MatchStatus.LIVE;
    }
}
