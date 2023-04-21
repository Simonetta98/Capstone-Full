package com.Capstone.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Capstone.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
