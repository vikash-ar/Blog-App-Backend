package com.blogapp.controllers;

import com.blogapp.model.RequestPage;
import com.blogapp.model.Response;
import com.blogapp.model.dto.CommentDto;
import com.blogapp.model.dto.PostDto;
import com.blogapp.services.CommentService;
import com.blogapp.services.FileService;
import com.blogapp.services.PostService;
import com.blogapp.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/posts")
public class PostController {
    private final PostService postService;
    private final FileService fileService;
    private final CommentService commentService;
    private final JwtUtils jwtUtils;
    @Autowired
    public PostController(PostService postService, FileService fileService, CommentService commentService, JwtUtils jwtUtils) {
        this.postService = postService;
        this.fileService = fileService;
        this.commentService = commentService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping
    public ResponseEntity<Response> createPost(@Valid @RequestBody PostDto postDto, @RequestParam(value = "categoryId", required = false) long categoryId, @RequestHeader String token) {
        long userId = jwtUtils.getUserIdFromToken(token);
        Response createPost = postService.createPost(postDto, userId, categoryId);
        return new ResponseEntity<>(createPost, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Response> getPostById(@PathVariable long postId) {
        Response postDto = postService.getPostById(postId);
        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Response>> getAllPosts(RequestPage pageRequest) {
        List<Response> result;
        if(!Objects.isNull(pageRequest.getTitle())) result = postService.getPostsByTitle(pageRequest.getTitle());
        else if(pageRequest.getUserId() != 0) result = postService.getPostsByUser(pageRequest.getUserId());
        else result = postService.getAllPosts(pageRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable long postId) {
        postService.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Response> updatePost(@RequestBody PostDto postDto, @PathVariable long postId) {
        Response updatedPost = postService.updatePost(postDto, postId);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<Response> createPostComment(@RequestBody CommentDto comment, @PathVariable long postId,  @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        long userId = jwtUtils.getUserIdFromToken(token);
        Response createComment = commentService.createComment(comment, postId, userId);
        return new ResponseEntity<>(createComment, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<Response>> getPostComments(@PathVariable long postId) {
        List<Response> postComments = commentService.getPostComments(postId);
        return new ResponseEntity<>(postComments, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable long postId, @PathVariable long commentId) {
        commentService.deleteComment(postId, commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Response> updateComment(@RequestBody CommentDto comment, @PathVariable long commentId,  @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        long userId = jwtUtils.getUserIdFromToken(token);
        Response updatedComment = commentService.updateComment(comment, commentId, userId);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @PostMapping("/{postId}/image-upload")
    public ResponseEntity<Response> uploadPostImage(@RequestParam("image") MultipartFile image, @PathVariable long postId) {
        String imagePath = fileService.uploadImageToCloud(image);
        Response updatedPost = postService.updatePostImage(imagePath, postId);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }
}
