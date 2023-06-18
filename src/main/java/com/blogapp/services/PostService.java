package com.blogapp.services;

import com.blogapp.model.RequestPage;
import com.blogapp.model.Response;
import com.blogapp.model.dto.PostDto;

import java.util.List;

public interface PostService {
    Response createPost(PostDto postDto, long userId, long categoryId);
    Response updatePost(PostDto postDto, long postId);
    Response updatePostImage(String imageUrl, long postId);
    void deletePost(long postId);
    Response getPostById(long postId);
    List<Response> getAllPosts(RequestPage pageRequest);
    List<Response> getPostsByUser(long userId);
    List<Response> getPostsByTitle(String keyword);
}
