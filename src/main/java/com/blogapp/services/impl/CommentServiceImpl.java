package com.blogapp.services.impl;

import com.blogapp.enums.ErrorCodes;
import com.blogapp.exceptions.BusinessException;
import com.blogapp.model.Response;
import com.blogapp.model.dao.Comment;
import com.blogapp.model.dao.Post;
import com.blogapp.model.dao.UserDao;
import com.blogapp.model.dto.CommentDto;
import com.blogapp.repository.CommentRepository;
import com.blogapp.repository.PostRepository;
import com.blogapp.repository.UserRepository;
import com.blogapp.services.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ObjectMapper objectMapper;
    @Autowired
    public CommentServiceImpl(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository, ObjectMapper objectMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public Response createComment(CommentDto commentDto, long postId, long userId) {
        UserDao user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCodes.USER_NOT_FOUND, HttpStatus.BAD_REQUEST));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCodes.POST_NOT_FOUND, HttpStatus.BAD_REQUEST));

        Comment comment = objectMapper.convertValue(commentDto, Comment.class);
        comment.setUser(user);

        post.getComments().add(comment);
        log.info("-------CommentServiceImpl:createComment:: new comment is created by user with id: {}, on post with id: {}-----", userId, postId);
        postRepository.save(post);

        return objectMapper.convertValue(comment, Response.class);
    }

    @Override
    public void deleteComment(long postId, long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCodes.COMMENT_NOT_FOUND, HttpStatus.BAD_REQUEST));
        commentRepository.delete(comment);
    }

    @Override
    public Response updateComment(CommentDto commentDto, long commentId, long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCodes.COMMENT_NOT_FOUND, HttpStatus.BAD_REQUEST));

        log.info("------CommentServiceImpl:updateComment-Before update comment : {} ------", comment.getContent());
        comment.setContent(commentDto.getContent());
        log.info("------CommentServiceImpl:updateComment-After update comment : {} ------", comment.getContent());

        comment = commentRepository.save(comment);
        return objectMapper.convertValue(comment, Response.class);
    }

    @Override
    public List<Response> getPostComments(long postId) {
        if(!postRepository.existsById(postId)) throw new BusinessException(ErrorCodes.POST_NOT_FOUND, HttpStatus.BAD_REQUEST);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCodes.POST_NOT_FOUND, HttpStatus.BAD_REQUEST));

        List<Comment> comments = post.getComments();
        log.info("-------CommentServiceImpl:getPostComments:: fetching all comments on post with id: {}-----", postId);
        return comments.stream().map(comment -> objectMapper.convertValue(comment, Response.class)).collect(Collectors.toList());
    }
}
