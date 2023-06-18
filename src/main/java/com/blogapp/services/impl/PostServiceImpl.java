package com.blogapp.services.impl;

import com.blogapp.enums.ErrorCodes;
import com.blogapp.exceptions.BusinessException;
import com.blogapp.model.RequestPage;
import com.blogapp.model.Response;
import com.blogapp.model.dao.Category;
import com.blogapp.model.dao.Post;
import com.blogapp.model.dao.UserDao;
import com.blogapp.model.dto.PostDto;
import com.blogapp.repository.CategoryRepository;
import com.blogapp.repository.PostRepository;
import com.blogapp.repository.UserRepository;
import com.blogapp.services.PostService;
import com.blogapp.utils.CommonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    @Autowired
    public PostServiceImpl(PostRepository postRepository, ObjectMapper objectMapper, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }


    @Override
    public Response createPost(PostDto postDto, long userId, long categoryId) {

        UserDao user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCodes.USER_NOT_FOUND, HttpStatus.BAD_REQUEST));

        Optional<Category> category = categoryRepository.findById(categoryId);

        Post post = objectMapper.convertValue(postDto, Post.class);
        post.setUser(user);

        if(category.isPresent()) post.getCategories().add(category.get());

        post = postRepository.save(post);
        log.info("-----PostServiceImpl:createPost::--new post has been mage by user with id: {}----", userId);

        return objectMapper.convertValue(post, Response.class);
    }

    @Override
    public Response updatePost(PostDto postDto, long postId) {

        Post post = this.postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCodes.POST_NOT_FOUND, HttpStatus.BAD_REQUEST));

        log.info("------PostServiceImpl:updatePost-Before update Post title : {} and content: {} ------", post.getTitle(), post.getContent());

        Post updatedResource = objectMapper.convertValue(postDto, Post.class);
        BeanUtils.copyProperties(updatedResource, post, CommonUtils.getNullPropertyNames(updatedResource));
        log.info("------PostServiceImpl:updatePost- update request for Post are  title : {} and content: {} ------", updatedResource.getTitle(), updatedResource.getContent());

        post.setId(postId);
        post = postRepository.save(post);
        log.info("------PostServiceImpl:updatePost-after update Post title : {} and content: {} ------", post.getTitle(), post.getContent());

        return objectMapper.convertValue(post, Response.class);
    }

    @Override
    public Response updatePostImage(String imageUrl, long postId) {
        Post post = this.postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCodes.POST_NOT_FOUND, HttpStatus.BAD_REQUEST));

        post.setImageName(imageUrl);
        post = postRepository.save(post);
        log.info("------PostServiceImpl:updatePostImage::---image has been updated of post with id: {}-----", postId);

        return objectMapper.convertValue(post, Response.class);
    }

    @Override
    public void deletePost(long postId) {
        Post post = this.postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCodes.POST_NOT_FOUND, HttpStatus.BAD_REQUEST));

        postRepository.delete(post);
    }

    @Override
    public Response getPostById(long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCodes.POST_NOT_FOUND, HttpStatus.BAD_REQUEST));

        log.info("-----PostServiceImpl:getPostById::--fetching post with id: {}----", postId);
        return objectMapper.convertValue(post, Response.class);
    }

    @Override
    public List<Response> getAllPosts(RequestPage pageRequest) {
        Sort sort = (pageRequest.getSortDir().equalsIgnoreCase("asc")) ? Sort.by(pageRequest.getSortBy()).ascending() : Sort.by(pageRequest.getSortBy()).descending();
        Pageable page = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sort);
        Page<Post> postsPage = postRepository.findAll(page);
        List<Post> postsList = postsPage.getContent();
        return postsList.stream().map(post -> objectMapper.convertValue(post, Response.class))
                .collect(Collectors.toList());
    }


    @Override
    public List<Response> getPostsByUser(long userId) {
        List<Post> posts = postRepository.findByUserId(userId);
        log.info("-----PostServiceImpl:getPostsByUser::--fetching post with User id: {}----", userId);

        return posts.stream().map(post -> objectMapper.convertValue(post, Response.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Response> getPostsByTitle(String keyword) {
        List<Post> posts = this.postRepository.findByTitleContaining(keyword);
        log.info("-----PostServiceImpl:getPostsByUser::--fetching post with title containing: {}----", keyword);

        return posts.stream().map(post-> objectMapper.convertValue(post, Response.class)).collect(Collectors.toList());
    }
}
