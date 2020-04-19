package com.albat.mobachir.network.managers;

import androidx.annotation.NonNull;

import com.albat.mobachir.App;
import com.albat.mobachir.network.interfaces.CountriesRetrieved;
import com.albat.mobachir.network.interfaces.FixturesRetrieved;
import com.albat.mobachir.network.interfaces.LeaguesRetrieved;
import com.albat.mobachir.network.interfaces.LeaguesWithFixturesRetrieved;
import com.albat.mobachir.network.interfaces.StagesRetrieved;
import com.albat.mobachir.network.interfaces.TopScorersRetrieved;
import com.albat.mobachir.network.models.Country;
import com.albat.mobachir.network.models.Fixture;
import com.albat.mobachir.network.models.League;
import com.albat.mobachir.network.models.LeaguesData;
import com.albat.mobachir.network.models.Stage;
import com.albat.mobachir.network.models.TopScorer;
import com.albat.mobachir.network.request.GetAvailableFixturesForGuessingRequest;
import com.albat.mobachir.network.request.GetFixturesRequest;
import com.albat.mobachir.network.request.GetLeagueWithFixturesRequest;
import com.albat.mobachir.network.request.GetStagesRequest;
import com.albat.mobachir.network.request.GetTopScorersRequest;
import com.albat.mobachir.network.response.CountriesResponse;
import com.albat.mobachir.network.response.FixturesResponse;
import com.albat.mobachir.network.response.LeaguesResponse;
import com.albat.mobachir.network.response.LeaguesWithFixturesResponse;
import com.albat.mobachir.network.response.StagesResponse;
import com.albat.mobachir.network.response.TopScorersResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaguesManager {

    public void getCountries(final CountriesRetrieved callback) {

        APIManager apiManager = APIManager.retrofit.create(APIManager.class);
        Call<CountriesResponse> call = apiManager.getCountries();
        call.enqueue(new Callback<CountriesResponse>() {
            @Override
            public void onResponse(@NonNull Call<CountriesResponse> call, @NonNull Response<CountriesResponse> response) {
                ArrayList<Country> countries = null;
                boolean success = false;
                String error = "خطأ في الإتصال";

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().countries != null) {
                            countries = response.body().countries;
                            success = true;
                        } else {
                            error = response.body().error;
                        }
                    }
                }

                if (callback != null)
                    callback.onCountriesRetrieved(countries, success, error);
            }

            @Override
            public void onFailure(@NonNull Call<CountriesResponse> call, @NonNull Throwable t) {
                if (callback != null)
                    callback.onCountriesRetrieved(null, false, "خطأ في الإتصال");
            }
        });
    }

    public void getLeagues(final LeaguesRetrieved callback) {
        APIManager apiManager = APIManager.retrofit.create(APIManager.class);
        Call<LeaguesResponse> call = apiManager.getLeagues();
        call.enqueue(new Callback<LeaguesResponse>() {
            @Override
            public void onResponse(@NonNull Call<LeaguesResponse> call, @NonNull Response<LeaguesResponse> response) {
                LeaguesData leaguesData = null;
                boolean success = false;
                String error = "خطأ في الإتصال";

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().leaguesData != null) {
                            leaguesData = response.body().leaguesData;
                            success = true;
                        } else {
                            error = response.body().error;
                        }
                    }
                }

                if (callback != null)
                    callback.onLeaguesRetrieved(leaguesData, success, error);
            }

            @Override
            public void onFailure(@NonNull Call<LeaguesResponse> call, @NonNull Throwable t) {
                if (callback != null)
                    callback.onLeaguesRetrieved(null, false, "خطأ في الإتصال");
            }
        });
    }

    public void getLeagueWithFixtures(String date, int mode, final LeaguesWithFixturesRetrieved callback) {
        getLeagueWithFixtures(date, mode, new ArrayList<Integer>(), callback);
    }

    public void getLeagueWithFixtures(String date, int mode, ArrayList<Integer> leaguesIds, final LeaguesWithFixturesRetrieved callback) {
        GetLeagueWithFixturesRequest request = new GetLeagueWithFixturesRequest();
        request.mode = mode;
        request.date = date;
        request.userId = App.getInstance().getUser() != null ? App.getInstance().getUser().id : 0;
        request.leaguesIds = leaguesIds;

        APIManager apiManager = APIManager.retrofit.create(APIManager.class);
        Call<LeaguesWithFixturesResponse> call = apiManager.getLeaguesWithFixtures(request);
        call.enqueue(new Callback<LeaguesWithFixturesResponse>() {
            @Override
            public void onResponse(@NonNull Call<LeaguesWithFixturesResponse> call, @NonNull Response<LeaguesWithFixturesResponse> response) {
                ArrayList<League> leagues = null;
                boolean success = false;
                String error = "خطأ في الإتصال";

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().leagues != null) {
                            leagues = response.body().leagues;
                            success = true;
                        } else {
                            error = response.body().error;
                        }
                    }
                }

                if (callback != null)
                    callback.onLeaguesWithFixturesRetrieved(leagues, success, error);
            }

            @Override
            public void onFailure(@NonNull Call<LeaguesWithFixturesResponse> call, @NonNull Throwable t) {
                if (callback != null)
                    callback.onLeaguesWithFixturesRetrieved(null, false, "خطأ في الإتصال");
            }
        });
    }

    public void getLeagueWithFixturesNow(final LeaguesWithFixturesRetrieved callback) {
        APIManager apiManager = APIManager.retrofit.create(APIManager.class);
        Call<LeaguesWithFixturesResponse> call = apiManager.getLeaguesWithFixturesNow();
        call.enqueue(new Callback<LeaguesWithFixturesResponse>() {
            @Override
            public void onResponse(@NonNull Call<LeaguesWithFixturesResponse> call, @NonNull Response<LeaguesWithFixturesResponse> response) {
                ArrayList<League> leagues = null;
                boolean success = false;
                String error = "خطأ في الإتصال";

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().leagues != null) {
                            leagues = response.body().leagues;
                            success = true;
                        } else {
                            error = response.body().error;
                        }
                    }
                }

                if (callback != null)
                    callback.onLeaguesWithFixturesRetrieved(leagues, success, error);
            }

            @Override
            public void onFailure(@NonNull Call<LeaguesWithFixturesResponse> call, @NonNull Throwable t) {
                if (callback != null)
                    callback.onLeaguesWithFixturesRetrieved(null, false, "خطأ في الإتصال");
            }
        });
    }

    public void getTopScorers(int seasonId, final TopScorersRetrieved callback) {
        GetTopScorersRequest request = new GetTopScorersRequest();
        request.seasonId = seasonId;

        APIManager apiManager = APIManager.retrofit.create(APIManager.class);
        Call<TopScorersResponse> call = apiManager.getTopScorers(request);
        call.enqueue(new Callback<TopScorersResponse>() {
            @Override
            public void onResponse(@NonNull Call<TopScorersResponse> call, @NonNull Response<TopScorersResponse> response) {
                ArrayList<TopScorer> topScorers = null;
                boolean success = false;
                String error = "خطأ في الإتصال";

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().topScorers != null) {
                            topScorers = response.body().topScorers;
                            success = true;
                        } else {
                            error = response.body().error;
                        }
                    }
                }

                if (callback != null)
                    callback.onTopScorersRetrieved(topScorers, success, error);
            }

            @Override
            public void onFailure(@NonNull Call<TopScorersResponse> call, @NonNull Throwable t) {
                if (callback != null)
                    callback.onTopScorersRetrieved(null, false, "خطأ في الإتصال");
            }
        });
    }

    public void getStagesWithStandings(int seasonId, final StagesRetrieved callback) {
        GetStagesRequest request = new GetStagesRequest();
        request.seasonId = seasonId;

        APIManager apiManager = APIManager.retrofit.create(APIManager.class);
        Call<StagesResponse> call = apiManager.getStagesWithStandings(request);
        call.enqueue(new Callback<StagesResponse>() {
            @Override
            public void onResponse(@NonNull Call<StagesResponse> call, @NonNull Response<StagesResponse> response) {
                ArrayList<Stage> stages = null;
                boolean success = false;
                String error = "خطأ في الإتصال";

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().stages != null) {
                            stages = response.body().stages;
                            success = true;
                        } else {
                            error = response.body().error;
                        }
                    }
                }

                if (callback != null)
                    callback.onStagesRetrieved(stages, success, error);
            }

            @Override
            public void onFailure(@NonNull Call<StagesResponse> call, @NonNull Throwable t) {
                if (callback != null)
                    callback.onStagesRetrieved(null, false, "خطأ في الإتصال");
            }
        });
    }

    public void getStages(int seasonId, final StagesRetrieved callback) {
        GetStagesRequest request = new GetStagesRequest();
        request.seasonId = seasonId;

        APIManager apiManager = APIManager.retrofit.create(APIManager.class);
        Call<StagesResponse> call = apiManager.getStages(request);
        call.enqueue(new Callback<StagesResponse>() {
            @Override
            public void onResponse(@NonNull Call<StagesResponse> call, @NonNull Response<StagesResponse> response) {
                ArrayList<Stage> stages = null;
                boolean success = false;
                String error = "خطأ في الإتصال";

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().stages != null) {
                            stages = response.body().stages;
                            success = true;
                        } else {
                            error = response.body().error;
                        }
                    }
                }

                if (callback != null)
                    callback.onStagesRetrieved(stages, success, error);
            }

            @Override
            public void onFailure(@NonNull Call<StagesResponse> call, @NonNull Throwable t) {
                if (callback != null)
                    callback.onStagesRetrieved(null, false, "خطأ في الإتصال");
            }
        });
    }

    public void getStageFixtures(int stageId, final FixturesRetrieved callback) {
        GetFixturesRequest request = new GetFixturesRequest();
        request.stageId = stageId;

        APIManager apiManager = APIManager.retrofit.create(APIManager.class);
        Call<FixturesResponse> call = apiManager.getStageFixtures(request);
        call.enqueue(new Callback<FixturesResponse>() {
            @Override
            public void onResponse(@NonNull Call<FixturesResponse> call, @NonNull Response<FixturesResponse> response) {
                ArrayList<Fixture> fixtures = null;
                boolean success = false;
                String error = "خطأ في الإتصال";

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().fixtures != null) {
                            fixtures = response.body().fixtures;
                            success = true;
                        } else {
                            error = response.body().error;
                        }
                    }
                }

                if (callback != null)
                    callback.onFixturesRetrieved(fixtures, success, error);
            }

            @Override
            public void onFailure(@NonNull Call<FixturesResponse> call, @NonNull Throwable t) {
                if (callback != null)
                    callback.onFixturesRetrieved(null, false, "خطأ في الإتصال");
            }
        });
    }

    public void getAvailableFixturesForGuessing(final FixturesRetrieved callback) {
        GetAvailableFixturesForGuessingRequest request = new GetAvailableFixturesForGuessingRequest();
        request.userId = App.getInstance().getUser().id;

        APIManager apiManager = APIManager.retrofit.create(APIManager.class);
        Call<FixturesResponse> call = apiManager.getAvailableFixturesForGuessing(request);
        call.enqueue(new Callback<FixturesResponse>() {
            @Override
            public void onResponse(@NonNull Call<FixturesResponse> call, @NonNull Response<FixturesResponse> response) {
                ArrayList<Fixture> fixtures = null;
                boolean success = false;
                String error = "خطأ في الإتصال";

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().fixtures != null) {
                            fixtures = response.body().fixtures;
                            success = true;
                        } else {
                            error = response.body().error;
                        }
                    }
                }

                if (callback != null)
                    callback.onFixturesRetrieved(fixtures, success, error);
            }

            @Override
            public void onFailure(@NonNull Call<FixturesResponse> call, @NonNull Throwable t) {
                if (callback != null)
                    callback.onFixturesRetrieved(null, false, "خطأ في الإتصال");
            }
        });
    }
}
