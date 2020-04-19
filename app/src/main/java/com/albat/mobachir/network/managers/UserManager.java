package com.albat.mobachir.network.managers;

import androidx.annotation.NonNull;

import com.albat.mobachir.App;
import com.albat.mobachir.Config;
import com.albat.mobachir.network.interfaces.AppVersionRetrieved;
import com.albat.mobachir.network.interfaces.GuessAdded;
import com.albat.mobachir.network.interfaces.GuessesRetrieved;
import com.albat.mobachir.network.interfaces.LeaderboardRetrieved;
import com.albat.mobachir.network.interfaces.MonthPrizesRetrieved;
import com.albat.mobachir.network.interfaces.PrizesRetrieved;
import com.albat.mobachir.network.interfaces.UserLoggedIn;
import com.albat.mobachir.network.interfaces.UserUpdated;
import com.albat.mobachir.network.interfaces.WinnersRetrieved;
import com.albat.mobachir.network.models.AppVersion;
import com.albat.mobachir.network.models.Guess;
import com.albat.mobachir.network.models.Leaderboard;
import com.albat.mobachir.network.models.MonthPrizes;
import com.albat.mobachir.network.models.Prize;
import com.albat.mobachir.network.models.User;
import com.albat.mobachir.network.models.Winner;
import com.albat.mobachir.network.request.AddCoinRequest;
import com.albat.mobachir.network.request.AddGuessRequest;
import com.albat.mobachir.network.request.GetGuessesRequest;
import com.albat.mobachir.network.request.GetLeaderboardRequest;
import com.albat.mobachir.network.request.GetMonthPrizesRequest;
import com.albat.mobachir.network.request.GetUserRequest;
import com.albat.mobachir.network.request.GetWinnersRequest;
import com.albat.mobachir.network.request.LoginRequest;
import com.albat.mobachir.network.request.RegisterRequest;
import com.albat.mobachir.network.response.AddGuessResponse;
import com.albat.mobachir.network.response.AppVersionResponse;
import com.albat.mobachir.network.response.GuessesResponse;
import com.albat.mobachir.network.response.LeaderboardResponse;
import com.albat.mobachir.network.response.MonthPrizesResponse;
import com.albat.mobachir.network.response.PrizesResponse;
import com.albat.mobachir.network.response.UserResponse;
import com.albat.mobachir.network.response.WinnersResponse;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserManager {

    public void login(int loginType, String loginId, String password, final UserLoggedIn callback) {
        LoginRequest request = new LoginRequest();
        request.loginType = loginType;
        request.loginId = loginId;
        request.password = password;

        APIManager apiManager = APIManager.retrofit.create(APIManager.class);
        Call<UserResponse> call = apiManager.login(request);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                User user = null;
                boolean success = false;
                String error = "خطأ في الإتصال";

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().user != null) {
                            user = response.body().user;
                            success = true;
                        } else {
                            error = response.body().error;
                        }
                    }
                }

                if (callback != null)
                    callback.onUserLoggedIn(user, success, error);
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                if (callback != null)
                    callback.onUserLoggedIn(null, false, "خطأ في الإتصال");
            }
        });
    }

    public void socialLogin(int loginType, String loginId, String name, String email, final UserLoggedIn callback) {
        LoginRequest request = new LoginRequest();
        request.loginType = loginType;
        request.loginId = loginId;
        request.name = name;
        request.email = email;

        APIManager apiManager = APIManager.retrofit.create(APIManager.class);
        Call<UserResponse> call = apiManager.socialLogin(request);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                User user = null;
                boolean success = false;
                String error = "خطأ في الإتصال";

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().user != null) {
                            user = response.body().user;
                            success = true;
                        } else {
                            error = response.body().error;
                        }
                    }
                }

                if (callback != null)
                    callback.onUserLoggedIn(user, success, error);
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                if (callback != null)
                    callback.onUserLoggedIn(null, false, "خطأ في الإتصال");
            }
        });
    }

    public void register(String name, String email, String password, final UserLoggedIn callback) {
        RegisterRequest request = new RegisterRequest();
        request.loginType = Config.LoginType.NORMAL.getValue();
        request.loginId = email;
        request.password = password;
        request.name = name;
        request.email = email;

        APIManager apiManager = APIManager.retrofit.create(APIManager.class);
        Call<UserResponse> call = apiManager.register(request);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                User user = null;
                boolean success = false;
                String error = "خطأ في الإتصال";

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().user != null) {
                            user = response.body().user;
                            success = true;
                        } else {
                            error = response.body().error;
                        }
                    }
                }

                if (callback != null)
                    callback.onUserLoggedIn(user, success, error);
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                if (callback != null)
                    callback.onUserLoggedIn(null, false, "خطأ في الإتصال");
            }
        });
    }

    public void getUser(final UserLoggedIn callback) {
        GetUserRequest request = new GetUserRequest();
        request.userId = App.getInstance().getUser().id;

        APIManager apiManager = APIManager.retrofit.create(APIManager.class);
        Call<UserResponse> call = apiManager.getUser(request);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                User user = null;
                boolean success = false;
                String error = "خطأ في الإتصال";

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().user != null) {
                            user = response.body().user;
                            success = true;
                        } else {
                            error = response.body().error;
                        }
                    }
                }

                if (callback != null)
                    callback.onUserLoggedIn(user, success, error);
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                if (callback != null)
                    callback.onUserLoggedIn(null, false, "خطأ في الإتصال");
            }
        });
    }

    public void getGuesses(final GuessesRetrieved callback) {
        GetGuessesRequest request = new GetGuessesRequest();
        request.userId = App.getInstance().getUser().id;

        APIManager apiManager = APIManager.retrofit.create(APIManager.class);
        Call<GuessesResponse> call = apiManager.getGuesses(request);
        call.enqueue(new Callback<GuessesResponse>() {
            @Override
            public void onResponse(@NonNull Call<GuessesResponse> call, @NonNull Response<GuessesResponse> response) {
                ArrayList<Guess> guesses = null;
                boolean success = false;
                String error = "خطأ في الإتصال";

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().guesses != null) {
                            guesses = response.body().guesses;
                            success = true;
                        } else {
                            error = response.body().error;
                        }
                    }
                }

                if (callback != null)
                    callback.onGuessesRetrieved(guesses, success, error);
            }

            @Override
            public void onFailure(@NonNull Call<GuessesResponse> call, @NonNull Throwable t) {
                if (callback != null)
                    callback.onGuessesRetrieved(null, false, "خطأ في الإتصال");
            }
        });
    }

    public void getLeaderboard(int monthNumber, final LeaderboardRetrieved callback) {
        GetLeaderboardRequest request = new GetLeaderboardRequest();
        request.monthNumber = monthNumber;
        request.userId = App.getInstance().getUser() != null ? App.getInstance().getUser().id : 0;
        APIManager apiManager = APIManager.retrofit.create(APIManager.class);
        Call<LeaderboardResponse> call = apiManager.getLeaderboard(request);
        call.enqueue(new Callback<LeaderboardResponse>() {
            @Override
            public void onResponse(@NonNull Call<LeaderboardResponse> call, @NonNull Response<LeaderboardResponse> response) {
                ArrayList<Leaderboard> leaderboard = null;
                boolean success = false;
                String error = "خطأ في الإتصال";

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().leaderboard != null) {
                            leaderboard = response.body().leaderboard;
                            success = true;
                        } else {
                            error = response.body().error;
                        }
                    }
                }

                if (callback != null)
                    callback.onLeaderboardRetrieved(leaderboard, success, error);
            }

            @Override
            public void onFailure(@NonNull Call<LeaderboardResponse> call, @NonNull Throwable t) {
                if (callback != null)
                    callback.onLeaderboardRetrieved(null, false, "خطأ في الإتصال");
            }
        });
    }

    public void addCoin(final UserUpdated callback) {
        AddCoinRequest request = new AddCoinRequest();
        request.userId = App.getInstance().getUser().id;

        APIManager apiManager = APIManager.retrofit.create(APIManager.class);
        Call<UserResponse> call = apiManager.addCoin(request);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                User user = null;
                boolean success = false;
                String error = "خطأ في الإتصال";

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().user != null) {
                            user = response.body().user;
                            success = true;
                        } else {
                            error = response.body().error;
                        }
                    }
                }

                if (callback != null)
                    callback.onUserUpdated(user, success, error);
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                if (callback != null)
                    callback.onUserUpdated(null, false, "خطأ في الإتصال");
            }
        });
    }

    public void addGuess(AddGuessRequest request, final GuessAdded callback) {

        APIManager apiManager = APIManager.retrofit.create(APIManager.class);
        Call<AddGuessResponse> call = apiManager.addGuess(request);
        call.enqueue(new Callback<AddGuessResponse>() {
            @Override
            public void onResponse(@NonNull Call<AddGuessResponse> call, @NonNull Response<AddGuessResponse> response) {
                User user = null;
                Guess guess = null;
                boolean success = false;
                String error = "خطأ في الإتصال";

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().addGuessResponseData != null && response.body().addGuessResponseData.user != null && response.body().addGuessResponseData.guess != null) {
                            user = response.body().addGuessResponseData.user;
                            guess = response.body().addGuessResponseData.guess;
                            success = true;
                        } else {
                            error = response.body().error;
                        }
                    }
                }

                if (callback != null)
                    callback.onGuessAdded(user, guess, success, error);
            }

            @Override
            public void onFailure(@NonNull Call<AddGuessResponse> call, @NonNull Throwable t) {
                if (callback != null)
                    callback.onGuessAdded(null, null, false, "خطأ في الإتصال");
            }
        });
    }

    public void getPrizes(final PrizesRetrieved callback) {
        APIManager apiManager = APIManager.retrofit.create(APIManager.class);
        Call<PrizesResponse> call = apiManager.getPrizes();
        call.enqueue(new Callback<PrizesResponse>() {
            @Override
            public void onResponse(@NonNull Call<PrizesResponse> call, @NonNull Response<PrizesResponse> response) {
                ArrayList<Prize> prizes = null;
                boolean success = false;
                String error = "خطأ في الإتصال";

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().prizes != null) {
                            prizes = response.body().prizes;
                            success = true;
                        } else {
                            error = response.body().error;
                        }
                    }
                }

                if (callback != null)
                    callback.onPrizesRetrieved(prizes, success, error);
            }

            @Override
            public void onFailure(@NonNull Call<PrizesResponse> call, @NonNull Throwable t) {
                if (callback != null)
                    callback.onPrizesRetrieved(null, false, "خطأ في الإتصال");
            }
        });
    }

    public void getWinners(int monthNumber, final WinnersRetrieved callback) {
        GetWinnersRequest request = new GetWinnersRequest();
        request.monthNumber = monthNumber;
        request.year = Calendar.getInstance().get(Calendar.YEAR);
        APIManager apiManager = APIManager.retrofit.create(APIManager.class);
        Call<WinnersResponse> call = apiManager.getWinners(request);
        call.enqueue(new Callback<WinnersResponse>() {
            @Override
            public void onResponse(@NonNull Call<WinnersResponse> call, @NonNull Response<WinnersResponse> response) {
                ArrayList<Winner> winners = null;
                boolean success = false;
                String error = "خطأ في الإتصال";

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().winners != null) {
                            winners = response.body().winners;
                            success = true;
                        } else {
                            error = response.body().error;
                        }
                    }
                }

                if (callback != null)
                    callback.onWinnersRetrieved(winners, success, error);
            }

            @Override
            public void onFailure(@NonNull Call<WinnersResponse> call, @NonNull Throwable t) {
                if (callback != null)
                    callback.onWinnersRetrieved(null, false, "خطأ في الإتصال");
            }
        });
    }

    public void getMonthPrizes(int monthNumber, final MonthPrizesRetrieved callback) {
        GetMonthPrizesRequest request = new GetMonthPrizesRequest();
        request.monthNumber = monthNumber;
        request.year = Calendar.getInstance().get(Calendar.YEAR);
        APIManager apiManager = APIManager.retrofit.create(APIManager.class);
        Call<MonthPrizesResponse> call = apiManager.getMonthPrizes(request);
        call.enqueue(new Callback<MonthPrizesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MonthPrizesResponse> call, @NonNull Response<MonthPrizesResponse> response) {
                MonthPrizes monthPrizes = null;
                boolean success = false;
                String error = "خطأ في الإتصال";

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().monthPrizes != null) {
                            monthPrizes = response.body().monthPrizes;
                            success = true;
                        } else {
                            error = response.body().error;
                        }
                    }
                }

                if (callback != null)
                    callback.onMonthPrizesRetrieved(monthPrizes, success, error);
            }

            @Override
            public void onFailure(@NonNull Call<MonthPrizesResponse> call, @NonNull Throwable t) {
                if (callback != null)
                    callback.onMonthPrizesRetrieved(null, false, "خطأ في الإتصال");
            }
        });
    }

    public void getAppVersion(final AppVersionRetrieved callback) {
        APIManager apiManager = APIManager.retrofit.create(APIManager.class);
        Call<AppVersionResponse> call = apiManager.getAppVersion();
        call.enqueue(new Callback<AppVersionResponse>() {
            @Override
            public void onResponse(@NonNull Call<AppVersionResponse> call, @NonNull Response<AppVersionResponse> response) {
                AppVersion appVersion = null;
                boolean success = false;
                String error = "خطأ في الإتصال";

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().appVersion != null) {
                            appVersion = response.body().appVersion;
                            success = true;
                        } else {
                            error = response.body().error;
                        }
                    }
                }

                if (callback != null)
                    callback.onAppVersionRetrieved(appVersion, success, error);
            }

            @Override
            public void onFailure(@NonNull Call<AppVersionResponse> call, @NonNull Throwable t) {
                if (callback != null)
                    callback.onAppVersionRetrieved(null, false, "خطأ في الإتصال");
            }
        });
    }
}
