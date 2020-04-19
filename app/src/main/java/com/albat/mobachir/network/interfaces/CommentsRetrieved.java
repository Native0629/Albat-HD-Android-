package com.albat.mobachir.network.interfaces;

import com.albat.mobachir.network.models.Comment;
import com.albat.mobachir.network.models.News;

import java.util.ArrayList;

public interface CommentsRetrieved {
    void onCommentsRetrieved(ArrayList<Comment> comments, boolean success, String error);
}
