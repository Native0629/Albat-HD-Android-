package com.albat.mobachir.network.managers;

import androidx.annotation.NonNull;

import com.albat.mobachir.App;
import com.albat.mobachir.network.interfaces.CommentAdded;
import com.albat.mobachir.network.interfaces.CommentsRetrieved;
import com.albat.mobachir.network.interfaces.FixtureRetrieved;
import com.albat.mobachir.network.models.Comment;
import com.albat.mobachir.network.models.Fixture;
import com.albat.mobachir.network.request.AddFixtureCommentRequest;
import com.albat.mobachir.network.request.GetFixtureCommentsRequest;
import com.albat.mobachir.network.request.GetFixtureRequest;
import com.albat.mobachir.network.response.AddCommentResponse;
import com.albat.mobachir.network.response.CommentsResponse;
import com.albat.mobachir.network.response.FixtureResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FixturesManager {
    private static final String TAG = "FixturesManager";

    public void addComment(int fixtureId, String content, final CommentAdded callback) {
        AddFixtureCommentRequest request = new AddFixtureCommentRequest();
        request.fixtureId = fixtureId;
        request.content = content;
        if (App.getInstance().getUser() != null)
            request.userId = App.getInstance().getUser().id;
        else
            request.author = "زائر";

        APIManager apiManager = APIManager.retrofit.create(APIManager.class);
        Call<AddCommentResponse> call = apiManager.addFixtureComment(request);
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

    public void getFixturesComments(int fixtureId, final CommentsRetrieved callback) {
        GetFixtureCommentsRequest request = new GetFixtureCommentsRequest();
        request.fixtureId = fixtureId;

        APIManager apiManager = APIManager.retrofit.create(APIManager.class);
        Call<CommentsResponse> call = apiManager.getFixtureComments(request);
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

    public void getFixture(int fixtureId, final FixtureRetrieved callback) {
        GetFixtureRequest request = new GetFixtureRequest();
        request.fixtureId = fixtureId;

        APIManager apiManager = APIManager.retrofit.create(APIManager.class);
        Call<FixtureResponse> call = apiManager.getFixture(request);
        call.enqueue(new Callback<FixtureResponse>() {
            @Override
            public void onResponse(@NonNull Call<FixtureResponse> call, @NonNull Response<FixtureResponse> response) {
                Fixture fixture = null;
                boolean success = false;
                String error = "خطأ في الإتصال";

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().fixture != null) {
                            fixture = response.body().fixture;
                            success = true;
                        } else {
                            error = response.body().error;
                        }
                    }
                }

                if (callback != null)
                    callback.onFixtureRetrieved(fixture, success, error);
            }

            @Override
            public void onFailure(@NonNull Call<FixtureResponse> call, @NonNull Throwable t) {
                if (callback != null)
                    callback.onFixtureRetrieved(null, false, "خطأ في الإتصال");
            }
        });
    }
}
