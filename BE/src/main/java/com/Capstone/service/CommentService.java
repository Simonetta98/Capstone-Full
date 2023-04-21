package com.Capstone.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.Capstone.entity.Comment;
import com.Capstone.entity.Post;
import com.Capstone.entity.Ptype;
import com.Capstone.entity.UserProfile;
import com.Capstone.exception.NotFoundEx;
import com.Capstone.repository.CommentRepository;
import com.Capstone.repository.UserProfileRepository;


@Service
public class CommentService {
	
	@Autowired CommentRepository repo;
	@Autowired UserProfileRepository usRepo;
	
	Logger log = LoggerFactory.getLogger(CommentService.class);
	
	public Comment createComment(Comment c, long id) {
		UserProfile up = usRepo.findById(id).get();
		c.setUserProfile(up);
		repo.save(c);
		return c;
	}
	
	public Comment removeComment(Long id) {
		Comment c = repo.findById(id).get();
		repo.deleteById(id);
		return c;
	}
	
	public Comment getCommentById(Long id) {
		Comment c = repo.findById(id).get();
		return c;
	}
	
	public Page<Comment> getAllComments(Pageable p){
		return (Page<Comment>) repo.findAll(p);
	}
	
	//PATCH
	public boolean partialUpdate(long id, String key, String value)
		      throws NotFoundEx {
		    log.info("Search id={}", id);
		    Optional<Comment> optional = repo.findById(id);
		    if (optional.isPresent()) {
		    	Comment c = optional.get();

		      if (key.equalsIgnoreCase("text")) {
		        log.info("Updating text");
		        c.setText(value);
		      }
		     
		      repo.save(c);
		      return true;
		    } else {
		      throw new NotFoundEx("RESOURCE_NOT_FOUND");
		    }
		  }

}
