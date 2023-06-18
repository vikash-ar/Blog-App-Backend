package com.blogapp.services;

import com.blogapp.model.Response;
import com.blogapp.model.dto.CommentDto;

import java.util.List;

public interface CommentService {
    Response createComment(CommentDto commentDto, long postId, long userId);
    void deleteComment(long postId, long commentId);
    Response updateComment(CommentDto comment, long commentId, long userId);
    List<Response> getPostComments(long postId);
}
