package com.albat.mobachir.util;

import android.content.Context;
import com.albat.mobachir.network.models.User;
import com.google.gson.annotations.SerializedName;
import net.grandcentrix.tray.TrayPreferences;
import org.json.JSONArray;
import java.util.ArrayList;

public class SharedPreferencesManager extends TrayPreferences {
    final String TAG = "SharedPreferencesManager";
    private static SharedPreferencesManager instance = null;

    public SharedPreferencesManager(final Context context) {
        super(context, MODULE, 1);
    }

    public static SharedPreferencesManager getInstance(Context context) {
        if (instance == null)
            instance = new SharedPreferencesManager(context.getApplicationContext());
        return instance;
    }

    private static final String MODULE = "AlbatHD";



    private final String USER_ID = "USER_ID";
    private final String USER_NAME = "USER_NAME";
    private final String USER_EMAIL = "USER_EMAIL";
    private final String USER_PICTURE = "USER_PICTURE";
    private final String USER_LOGIN_TYPE = "USER_LOGIN_TYPE";
    private final String USER_LOGIN_ID = "USER_LOGIN_ID";
    private final String USER_GOLDEN_POINTS = "USER_GOLDEN_POINTS";

    private final String LAST_COUNTRY_ID = "LAST_COUNTRY_ID";

    private final String FAVORITE_LEAGUES = "FAVORITE_LEAGUES";

    @SerializedName("Id")
    public int id;
    @SerializedName("LoginType")
    public int loginType;
    @SerializedName("LoginId")
    public String loginId;
    @SerializedName("Email")
    public String email;
    @SerializedName("Name")
    public String name;
    @SerializedName("GoldenPoints")
    public int goldenPoints;

    public int getUserId() {
        return getInt(USER_ID, 0);
    }

    public void setUserId(int userId) {
        put(USER_ID, userId);
    }

    public boolean isLoggedIn() {
        return getUserId() != 0;
    }

    public void saveUser(User user) {
        put(USER_ID, user.id);
        put(USER_NAME, user.name);
        put(USER_EMAIL, user.email);
        put(USER_LOGIN_TYPE, user.loginType);
        put(USER_LOGIN_ID, user.loginId);
        put(USER_GOLDEN_POINTS, user.goldenPoints);
    }

    public User getUser() {
        if (!isLoggedIn())
            return null;

        User user = new User();
        user.id = getInt(USER_ID, 0);
        user.name = getString(USER_NAME, "");
        user.email = getString(USER_EMAIL, "");
        user.loginType = getInt(USER_LOGIN_TYPE, 0);
        user.loginId = getString(USER_LOGIN_ID, "");
        user.goldenPoints = getInt(USER_GOLDEN_POINTS, 0);

        return user;
    }

    public boolean addToFavoriteLeague(int leagueId) {
        try {
            JSONArray leaguesIdsArr = new JSONArray(getString(FAVORITE_LEAGUES, "[]"));
            JSONArray updatedArr = new JSONArray();
            boolean removed = false;
            for (int i = 0; i < leaguesIdsArr.length(); i++) {
                if (leaguesIdsArr.getInt(i) == leagueId) {
                    removed = true;
                } else
                    updatedArr.put(leaguesIdsArr.get(i));
            }
            if (!removed)
                updatedArr.put(leagueId);
            put(FAVORITE_LEAGUES, updatedArr.toString());
        } catch (Exception e) {

        }
        return false;
    }

    public ArrayList<Integer> getFavoriteIds() {
        ArrayList<Integer> ids = new ArrayList<>();
        try {
            JSONArray leaguesIdsArr = new JSONArray(getString(FAVORITE_LEAGUES, "[]"));
            for (int i = 0; i < leaguesIdsArr.length(); i++) {
                ids.add(leaguesIdsArr.getInt(i));
            }
        } catch (Exception e) {

        }
        return ids;
    }

    public boolean isInsideFavorite(int leagueId) {
        try {
            JSONArray leaguesIdsArr = new JSONArray(getString(FAVORITE_LEAGUES, "[]"));
            for (int i = 0; i < leaguesIdsArr.length(); i++) {
                if (leaguesIdsArr.getInt(i) == leagueId) {
                    return true;
                }
            }
        } catch (Exception e) {

        }
        return false;
    }

    public void setLastCountryId(int countryId) {
        put(LAST_COUNTRY_ID, countryId);
    }

    public int getLastCountryId() {
        return getInt(LAST_COUNTRY_ID, 0);
    }


    public void logout() {
        remove(USER_ID);
        remove(USER_NAME);
        remove(USER_PICTURE);
        remove(USER_EMAIL);
        remove(USER_LOGIN_TYPE);
        remove(USER_LOGIN_ID);
        remove(USER_GOLDEN_POINTS);
        remove(LAST_COUNTRY_ID);
    }
}
