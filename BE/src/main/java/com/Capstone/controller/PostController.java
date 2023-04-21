package com.Capstone.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Capstone.entity.Comment;
import com.Capstone.entity.Post;
import com.Capstone.exception.NotFoundEx;
import com.Capstone.exception.NotYetImplementedEx;
import com.Capstone.payload.PatchDto;
import com.Capstone.payload.PostDto;
import com.Capstone.service.CommentService;
import com.Capstone.service.PostService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/post")
public class PostController {
 
	    @Autowired PostService service;
	    @Autowired CommentService cService;
	
	    @PostMapping("/create/{id}")  //id profilo
	    @PreAuthorize("hasRole('USER')")
	    public ResponseEntity<Post> create(@RequestBody PostDto post, @PathVariable long id){
	        return new ResponseEntity<Post>(service.createPost(post, id), HttpStatus.OK);
	    }
	    
	    /////COMMENTS
	    @PostMapping("/comment/{idPost}/{idProfile}")  
	    @PreAuthorize("hasRole('USER')")
	    public ResponseEntity<Comment> createComment(@RequestBody Comment c, @PathVariable long idPost, @PathVariable long idProfile){
	        return new ResponseEntity<Comment>(service.createComment(c, idPost, idProfile), HttpStatus.OK);
	    }
	    @GetMapping("/all/comments/{idPost}")
	    @PreAuthorize("hasRole('USER')")
	    public ResponseEntity<Set<Comment>> getAllComments(@PathVariable long idPost) {
	        return new ResponseEntity<Set<Comment>>(service.getAllComments(idPost), HttpStatus.OK);
	    }
	    @DeleteMapping("/comment/delete/{id}/{idPost}")  
	    @PreAuthorize("hasRole('USER')")
	    public ResponseEntity<Comment> deleteComment(@PathVariable long id, @PathVariable long idPost){
	        return new ResponseEntity<Comment>(service.deleteComment(id, idPost), HttpStatus.OK);
	    }
	    //////////
		
	    @GetMapping("/all/{page}")
	    @PreAuthorize("hasRole('USER')")
	    public ResponseEntity<Page<Post>> getAllPosts(@PathVariable int page, @RequestParam(defaultValue = "10") int size) {
	        Pageable sorting= PageRequest.of(page, size, Sort.by("id").ascending());
	        return new ResponseEntity<Page<Post>>(service.getAllPosts(sorting), HttpStatus.OK);
	    }

	    @GetMapping("/{id}")
	    @PreAuthorize("hasRole('USER')")
	    public ResponseEntity<Post> getPost(@PathVariable long id){
	        return new ResponseEntity<Post>(service.getUserPostById(id), HttpStatus.OK);
	    }
	    
	  //PATCH
	    @PatchMapping("/update/{id}")
	    @PreAuthorize("hasRole('USER')")
	    public ResponseEntity<Boolean> updatePartially(@PathVariable(name = "id") long id,
	        @RequestBody PatchDto dto) throws NotYetImplementedEx, NotFoundEx {
	      // skipping validations 
	      if (dto.getOp().equalsIgnoreCase("update")) {
	        boolean result = service.partialUpdate(id, dto.getKey(), dto.getValue());
	        return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
	      } else {
	        throw new NotYetImplementedEx("NOT_YET_IMPLEMENTED");
	      }
	    }
	    
	    @DeleteMapping ("/delete/{id}") 
	    @PreAuthorize("hasRole('USER')")
	    public ResponseEntity<Post> deletePost(@PathVariable long id){
	        return new ResponseEntity<Post>(service.removePost(id), HttpStatus.OK);}
}
