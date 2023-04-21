package com.Capstone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Capstone.entity.Post;
import com.Capstone.entity.Ptype;

public interface PostRepository extends JpaRepository<Post, Long> {

	public List<Post> findBypType(Ptype pType);
	
	public List<Post> findByTitle(String title);
	
	Boolean existsBypType(Ptype pType);
	
	Boolean existsByTitle(String title);
	
}
