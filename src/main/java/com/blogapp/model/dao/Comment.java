package com.blogapp.model.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String content;
    @Column(name = "created_at")
    @CreatedDate
    protected Long createdAt = Instant.now().toEpochMilli();
    @Column(name = "updated_at")
    @LastModifiedDate
    protected Long updatedAt = Instant.now().toEpochMilli();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private UserDao user;
}
