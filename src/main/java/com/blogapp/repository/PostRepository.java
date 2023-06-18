package com.blogapp.repository;

import com.blogapp.model.dao.Category;
import com.blogapp.model.dao.Post;
import com.blogapp.model.dao.UserDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(long userId);
//    List<Post> findByCategory(Category category);
    List<Post> findByTitleContaining(String keyword);
//    List<Post> findBy(String keyword);


//	@Query("select p from Post p where p.title like :key")
//	List<Post> searchByTitle(@Param("key") String title);

}
