package com.blogapp.repository;

import com.blogapp.model.dao.UserDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDao, Long> {
    Optional<UserDao> findByEmail(String email);
    boolean existsByEmail(String email);
}
