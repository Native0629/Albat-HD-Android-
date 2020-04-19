package com.albat.mobachir.network.interfaces;

import com.albat.mobachir.network.models.Comment;

public interface CommentAdded {
    void onCommentAdded(Comment comment, boolean success, String error);
}
