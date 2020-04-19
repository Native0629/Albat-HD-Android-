package com.albat.mobachir.network.managers;

import androidx.annotation.NonNull;

import com.albat.mobachir.App;
import com.albat.mobachir.network.interfaces.CommentAdded;
import com.albat.mobachir.network.interfaces.CommentsRetrieved;
import com.albat.mobachir.network.interfaces.NewLikeToggled;
import com.albat.mobachir.network.interfaces.NewsRetrieved;
import com.albat.mobachir.network.models.Comment;
import com.albat.mobachir.network.models.News;
import com.albat.mobachir.network.request.AddCommentRequest;
import com.albat.mobachir.network.request.GetNewsCommentsRequest;
import com.albat.mobachir.network.request.GetNewsRequest;
import com.albat.mobachir.network.request.ToggleNewsLikeRequest;
import com.albat.mobachir.network.response.AddCommentResponse;
import com.albat.mobachir.network.response.CommentsResponse;
import com.albat.mobachir.network.response.NewsResponse;
import com.albat.mobachir.network.response.ToggleNewsResponse;
import com.albat.mobachir.util.CLog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsManager {
    private static final String TAG = "NewsManager";

    public void getNews(final NewsRetrieved callback) {
        GetNewsRequest request = new GetNewsRequest();
        if (App.getInstance().getUser() != null)
            request.userId = App.getInstance().getUser().id;
        else
            request.userId = 0;

        APIManager apiManager = APIManager.retrofit.create(APIManager.class);
        Call<NewsResponse> call = apiManager.getNews(request);
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {
                ArrayList<News> news = null;
                boolean success = false;
                String error = "خطأ في الإتصال";

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().news != null) {
                            news = response.body().news;
                            success = true;
                        } else {
                            error = response.body().error;
                        }
                    }
                }

                if (callback != null)
                    callback.onNewsRetrieved(news, success, error);
            }

            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
                if (callback != null)
                    callback.onNewsRetrieved(null, false, "خطأ في الإتصال");
            }
        });
    }

    public void addComment(int newsId, String content, final CommentAdded callback) {
        AddCommentRequest request = new AddCommentRequest();
        request.newsId = newsId;
        request.content = content;
        if (App.getInstance().getUser() != null)
            request.userId = App.getInstance().getUser().id;
        else
            request.author = "زائر";

        APIManager apiManager = APIManager.retrofit.create(APIManager.class);
        Call<AddCommentResponse> call = apiManager.addComment(request);
        call.enqueue(new Callback<AddCommentResponse>() {
            @Override
            public void onResponse(@NonNull Call<AddCommentResponse> call, @NonNull Response<AddCommentResponse> response) {
                Comment comment = null;
                boolean success = false;
                String error = "خطأ في الإتصال";

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().comment != null) {
                            comment = response.body().comment;
                            success = true;
                        } else {
                            error = response.body().error;
                        }
                    }
                }

                if (callback != null)
                    callback.onCommentAdded(comment, success, error);
            }

            @Override
            public void onFailure(@NonNull Call<AddCommentResponse> call, @NonNull Throwable t) {
                if (callback != null)
                    callback.onCommentAdded(null, false, "خطأ في الإتصال");
            }
        });
    }

    public void getNewsComments(int newsId, final CommentsRetrieved callback) {
        GetNewsCommentsRequest request = new GetNewsCommentsRequest();
        request.newsId = newsId;

        APIManager apiManager = APIManager.retrofit.create(APIManager.class);
        Call<CommentsResponse> call = apiManager.getNewsComments(request);
        call.enqueue(new Callback<CommentsResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommentsResponse> call, @NonNull Response<CommentsResponse> response) {
                ArrayList<Comment> comments = null;
                boolean success = false;
                String error = "خطأ في الإتصال";

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().comments != null) {
                            comments = response.body().comments;
                            success = true;
                        } else {
                            error = response.body().error;
                        }
                    }
                }

                if (callback != null)
                    callback.onCommentsRetrieved(comments, success, error);
            }

            @Override
            public void onFailure(@NonNull Call<CommentsResponse> call, @NonNull Throwable t) {
                if (callback != null)
                    callback.onCommentsRetrieved(null, false, "خطأ في الإتصال");
            }
        });
    }

    public void toggleLike(int newsId, final NewLikeToggled callback) {
        ToggleNewsLikeRequest request = new ToggleNewsLikeRequest();
        request.newsId = newsId;
        if (App.getInstance().getUser() != null)
            request.userId = App.getInstance().getUser().id;

        APIManager apiManager = APIManager.retrofit.create(APIManager.class);
        Call<ToggleNewsResponse> call = apiManager.toggleNewsLike(request);
        call.enqueue(new Callback<ToggleNewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<ToggleNewsResponse> call, @NonNull Response<ToggleNewsResponse> response) {
                boolean likedByMe = false;
                int likes = 0;
                boolean success = false;
                String error = "خطأ في الإتصال";

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().toggleNewsResponseData != null) {
                            CLog.e(TAG, response.body().toggleNewsResponseData.likedByMe);
                            CLog.e(TAG, response.body().toggleNewsResponseData.likes);
                            likedByMe = response.body().toggleNewsResponseData.likedByMe;
                            likes = response.body().toggleNewsResponseData.likes;
                            success = true;
                        } else {
                            error = response.body().error;
                        }
                    }
                }

                if (callback != null)
                    callback.onNewLikeToggled(likedByMe, likes, success, error);
            }

            @Override
            public void onFailure(@NonNull Call<ToggleNewsResponse> call, @NonNull Throwable t) {
                if (callback != null)
                    callback.onNewLikeToggled(false, 0, false, "خطأ في الإتصال");
            }
        });
    }
}
