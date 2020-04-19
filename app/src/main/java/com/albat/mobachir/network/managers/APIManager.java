package com.albat.mobachir.network.managers;

import com.albat.mobachir.Config;
import com.albat.mobachir.network.models.AppVersion;
import com.albat.mobachir.network.request.AddCoinRequest;
import com.albat.mobachir.network.request.AddCommentRequest;
import com.albat.mobachir.network.request.AddFixtureCommentRequest;
import com.albat.mobachir.network.request.AddGuessRequest;
import com.albat.mobachir.network.request.GetAvailableFixturesForGuessingRequest;
import com.albat.mobachir.network.request.GetFixtureCommentsRequest;
import com.albat.mobachir.network.request.GetFixtureRequest;
import com.albat.mobachir.network.request.GetFixturesRequest;
import com.albat.mobachir.network.request.GetGuessesRequest;
import com.albat.mobachir.network.request.GetLeaderboardRequest;
import com.albat.mobachir.network.request.GetLeagueWithFixturesRequest;
import com.albat.mobachir.network.request.GetLeaguesRequest;
import com.albat.mobachir.network.request.GetMonthPrizesRequest;
import com.albat.mobachir.network.request.GetNewsCommentsRequest;
import com.albat.mobachir.network.request.GetNewsRequest;
import com.albat.mobachir.network.request.GetStagesRequest;
import com.albat.mobachir.network.request.GetTopScorersRequest;
import com.albat.mobachir.network.request.GetUserRequest;
import com.albat.mobachir.network.request.GetWinnersRequest;
import com.albat.mobachir.network.request.LoginRequest;
import com.albat.mobachir.network.request.RegisterRequest;
import com.albat.mobachir.network.request.ToggleNewsLikeRequest;
import com.albat.mobachir.network.response.AddCommentResponse;
import com.albat.mobachir.network.response.AddGuessResponse;
import com.albat.mobachir.network.response.AppVersionResponse;
import com.albat.mobachir.network.response.CommentsResponse;
import com.albat.mobachir.network.response.CountriesResponse;
import com.albat.mobachir.network.response.FixtureResponse;
import com.albat.mobachir.network.response.FixturesResponse;
import com.albat.mobachir.network.response.GuessesResponse;
import com.albat.mobachir.network.response.LeaderboardResponse;
import com.albat.mobachir.network.response.LeaguesResponse;
import com.albat.mobachir.network.response.LeaguesWithFixturesResponse;
import com.albat.mobachir.network.response.MonthPrizesResponse;
import com.albat.mobachir.network.response.NewsResponse;
import com.albat.mobachir.network.response.PrizesResponse;
import com.albat.mobachir.network.response.ToggleNewsResponse;
import com.albat.mobachir.network.response.TopScorersResponse;
import com.albat.mobachir.network.response.StagesResponse;
import com.albat.mobachir.network.response.UserResponse;
import com.albat.mobachir.network.response.WinnersResponse;
import com.albat.mobachir.network.utils.BooleanTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIManager {

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(2, TimeUnit.MINUTES)
            .addInterceptor(new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(boolean.class, new BooleanTypeAdapter())
            .setLenient()
            .create();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Config.API_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build();


    @POST("getUser")
    Call<UserResponse> getUser(@Body GetUserRequest body);

    @POST("getAppVersion")
    Call<AppVersionResponse> getAppVersion();

    @POST("login")
    Call<UserResponse> login(@Body LoginRequest body);

    @POST("socialLogin")
    Call<UserResponse> socialLogin(@Body LoginRequest body);

    @POST("register")
    Call<UserResponse> register(@Body RegisterRequest body);

    @POST("getNews")
    Call<NewsResponse> getNews(@Body GetNewsRequest body);

    @POST("toggleNewsLike")
    Call<ToggleNewsResponse> toggleNewsLike(@Body ToggleNewsLikeRequest body);

    @POST("addComment")
    Call<AddCommentResponse> addComment(@Body AddCommentRequest body);

    @POST("getNewsComments")
    Call<CommentsResponse> getNewsComments(@Body GetNewsCommentsRequest body);

    @POST("addFixtureComment")
    Call<AddCommentResponse> addFixtureComment(@Body AddFixtureCommentRequest body);

    @POST("getFixtureComments")
    Call<CommentsResponse> getFixtureComments(@Body GetFixtureCommentsRequest body);

    @POST("getFixture")
    Call<FixtureResponse> getFixture(@Body GetFixtureRequest body);

    @POST("getCountries")
    Call<CountriesResponse> getCountries();

    @POST("getLeagues")
    Call<LeaguesResponse> getLeagues();

    @POST("getLeaguesWithFixtures")
    Call<LeaguesWithFixturesResponse> getLeaguesWithFixtures(@Body GetLeagueWithFixturesRequest body);

    @POST("getLeaguesWithFixturesNow")
    Call<LeaguesWithFixturesResponse> getLeaguesWithFixturesNow();

    @POST("getTopScorers")
    Call<TopScorersResponse> getTopScorers(@Body GetTopScorersRequest body);

    @POST("getStagesWithStandings")
    Call<StagesResponse> getStagesWithStandings(@Body GetStagesRequest body);

    @POST("getStages")
    Call<StagesResponse> getStages(@Body GetStagesRequest body);

    @POST("getStageFixtures")
    Call<FixturesResponse> getStageFixtures(@Body GetFixturesRequest body);

    @POST("getAvailableFixturesForGuessing")
    Call<FixturesResponse> getAvailableFixturesForGuessing(@Body GetAvailableFixturesForGuessingRequest body);

    @POST("getGuesses")
    Call<GuessesResponse> getGuesses(@Body GetGuessesRequest body);

    @POST("getLeaderboard")
    Call<LeaderboardResponse> getLeaderboard(@Body GetLeaderboardRequest body);

    @POST("addCoin")
    Call<UserResponse> addCoin(@Body AddCoinRequest body);

    @POST("addGuess")
    Call<AddGuessResponse> addGuess(@Body AddGuessRequest body);

    @POST("getPrizes")
    Call<PrizesResponse> getPrizes();

    @POST("getWinners")
    Call<WinnersResponse> getWinners(@Body GetWinnersRequest body);

    @POST("getMonthPrizes")
    Call<MonthPrizesResponse> getMonthPrizes(@Body GetMonthPrizesRequest body);
}
