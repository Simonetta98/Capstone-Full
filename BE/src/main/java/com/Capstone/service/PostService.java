package com.Capstone.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import com.Capstone.payload.PostDto;
import com.Capstone.repository.PostRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PostService {

	@Autowired PostRepository repo;
	@Autowired UserProfileService profileSrv;
	@Autowired CommentService commentSrv;
	
	Logger log = LoggerFactory.getLogger(PostService.class);
	
	public Post createPost(PostDto p, long id) {
		UserProfile profile = profileSrv.getUserProfileById(id);
		Post post = new Post();
		post.setTitle(p.getTitle());
		post.setDescription(p.getDescription());
		if(p.getPType() == Ptype.PRIVATE.toString()) {
			post.setPType(Ptype.PRIVATE);
		}else {
			post.setPType(Ptype.PUBLIC);
		}
		post.setGithub(p.getGithub());
		
		profile.getPosts().add(post);
		profileSrv.updateUserProfile(profile);
		return post;
		//repo.save(p);
		//return p;
	}
	
	////COMMENTS ON POST
	public Comment createComment(Comment c, long idPost, long idProfile) {
		Post p = repo.findById(idPost).get();
		UserProfile up = profileSrv.getUserProfileById(idProfile);
		c.setUserProfile(up);
		p.getComments().add(c);
		repo.save(p);
		return c;
	}
	public Set<Comment> getAllComments(long idPost){
		Post p = repo.findById(idPost).get();
		Set<Comment> comments = p.getComments();
		return comments;
	}
	public Comment deleteComment(long idComment, long idPost) {
		Post p = repo.findById(idPost).get();
		Comment c = commentSrv.getCommentById(idComment);
		p.getComments().removeIf(e -> e.getId().equals(idComment));
		commentSrv.removeComment(idComment);
		repo.save(p);
		return c;
	}
	///////////////
	
	public void createAll(List<Post>list){
		repo.saveAll(list);
	}
	
	public Post getUserPostById(Long id) {
		if(!repo.existsById(id)) {
			throw new EntityNotFoundException("Questo Post non esiste");
		}
		Post p = repo.findById(id).get();
		return p;
	}
	
	public List<Post> getPostsByPtype(Ptype type) {
		if(!repo.existsBypType(type)) {
			throw new EntityNotFoundException("Nessun Post trovato");
		}
		List<Post> p = repo.findBypType(type);
		return p;
	}
	
	public List<Post> getPostsByTitle(String title) {
		if(!repo.existsByTitle(title)) {
			throw new EntityNotFoundException("Nessun Post trovato");
		}
		List<Post> p = repo.findByTitle(title);
		return p;
	}
	
	public Post updatePost(Post d) {
		if(!repo.existsById(d.getId())) {
			throw new EntityNotFoundException("Questo Post non esiste");
		}
		repo.save(d);
		return d;
	}
	
	//PATCH
		public boolean partialUpdate(long id, String key, String value)
			      throws NotFoundEx {
			    log.info("Search id={}", id);
			    Optional<Post> optional = repo.findById(id);
			    if (optional.isPresent()) {
			      Post post = optional.get();

			      if (key.equalsIgnoreCase("description")) {
			        log.info("Updating description");
			        post.setDescription(value);
			      }
			      if (key.equalsIgnoreCase("title")) {
			        log.info("Updating title");
			        post.setTitle(value);
			      }
			      if (key.equalsIgnoreCase("pType")) {
				        log.info("Updating type");
				        post.setPType(Ptype.valueOf(value));
				      }
			      if (key.equalsIgnoreCase("github")) {
				        log.info("Updating github");
				        post.setGithub(value);
				      }

			      repo.save(post);
			      return true;
			    } else {
			      throw new NotFoundEx("RESOURCE_NOT_FOUND");
			    }
			  }
	
	public Post removePost(Long id) {
		if(!repo.existsById(id)) {
			throw new EntityNotFoundException("Questo Post non esiste");
		}
		Post post = repo.findById(id).get();
	
		List<UserProfile> profiles = profileSrv.getUserProfileByLikes(id);
		for (UserProfile profile : profiles) {
			profile.getLikes().removeIf(e -> e.getId().equals(id));
			profileSrv.updateUserProfile(profile);
		}
		repo.delete(post);
		return post;
	}
	
	public Page<Post> getAllPosts(Pageable p){
		return (Page<Post>) repo.findAll(p);
	}
}
