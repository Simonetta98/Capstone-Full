package com.Capstone.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.Capstone.entity.Gender;
import com.Capstone.entity.Post;
import com.Capstone.entity.Ptype;
import com.Capstone.entity.User;
import com.Capstone.entity.UserProfile;
import com.Capstone.exception.NotFoundEx;
import com.Capstone.repository.PostRepository;
import com.Capstone.repository.UserProfileRepository;
import com.Capstone.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserProfileService {

	@Autowired UserProfileRepository repo;
	@Autowired UserRepository userRepo;
	@Autowired PostRepository postRepo;
	Logger log = LoggerFactory.getLogger(UserProfileService.class);
	
	
	public UserProfile createUserProfile(UserProfile d) {
		repo.save(d);
		return d;
	}
	
	public void createAll(List<UserProfile>list){
		repo.saveAll(list);
	}
	
	public Post createLike(Long up, Long post) {
		
		UserProfile profilo = repo.findById(up).get();
		Post newLike = postRepo.findById(post).get();
		newLike.setIsLiked("yes");
	 
		Set<Post> likesNew = new HashSet<Post>();
		
		Set<Post> likesOld = profilo.getLikes();
		likesOld.forEach(e-> likesNew.add(e));
		likesNew.add(newLike);
		
		//un Profilo(User) mette mi piace a dei post
		profilo.setLikes(likesNew);
		this.updateUserProfile(profilo); 
		this.postRepo.save(newLike);
		return  newLike;
	}
	
	public Set<Post> getUserProfileLikedPosts(long id) {
		UserProfile p = repo.findById(id).get();
		Set<Post> posts = p.getLikes();
		return posts;
	}
	
	public UserProfile getUserProfileById(Long id) {
		if(!repo.existsById(id)) {
			throw new EntityNotFoundException("Questo Profilo non esiste");
		}
		UserProfile u = repo.findById(id).get();
		return u;
	}
	
	public UserProfile getUserProfileByUsername(String username) {
		if(!repo.existsByUserUsername(username)) {
			throw new EntityNotFoundException("Profilo non trovato");
		}
		UserProfile u = repo.findByUsername(username).get();
		return u;
	}
	
	public Set<Post> getUserProfilePosts(Long id) {
		if(!repo.existsById(id)) {
			throw new EntityNotFoundException("Questo Profilo non esiste");
		}
		UserProfile u = repo.findById(id).get();
		Set<Post> posts = u.getPosts();
		return posts;
	}
	
	public UserProfile getProfileByPost(long id) {
		Post p = postRepo.findById(id).get();
		UserProfile up = repo.findByPosts(p);
		return up;
	}
	
	public List<UserProfile> getUserProfileByLikes(long id){
		Post post = postRepo.findById(id).get();
		List<Post> likes = new ArrayList<Post>();
		likes.add(post);
		List<UserProfile> p = repo.findByLikesIn(likes);
		return p;
	}
	
	public UserProfile updateUserProfile(UserProfile d) {
		if(!repo.existsById(d.getId())) {
			throw new EntityNotFoundException("Questo Profilo non esiste");
		}
		repo.save(d);
		return d;
	}
	
	//PATCH
	public boolean partialUpdate(long id, String key, String value)
		      throws NotFoundEx {
		    log.info("Search id={}", id);
		    Optional<UserProfile> optional = repo.findById(id);
		    if (optional.isPresent()) {
		      UserProfile profile = optional.get();

		      if (key.equalsIgnoreCase("description")) {
		        log.info("Updating description");
		        profile.setDescription(value);
		      }
		      if (key.equalsIgnoreCase("birthdate")) {
		        log.info("Updating birthdate");
		        profile.setBirthdate(LocalDate.parse(value));
		      }
		      if (key.equalsIgnoreCase("github")) {
			        log.info("Updating github");
			        profile.setGithub(value);
			      }
		      if (key.equalsIgnoreCase("linkedin")) {
			        log.info("Updating linkedin");
			        profile.setLinkedin(value);
			      }
		      if (key.equalsIgnoreCase("gender")) {
		  			profile.setGender(Gender.valueOf(value));
			        log.info("Updating gender");
			      }

		      repo.save(profile);
		      return true;
		    } else {
		      throw new NotFoundEx("RESOURCE_NOT_FOUND");
		    }
		  }
	
	public User removeUserProfile(Long id) {
		if(!repo.existsById(id)) {
			throw new EntityNotFoundException("Questo Profilo non esiste");
		}
		User u = repo.findById(id).get().getUser();
		u.setUserProfile(null);
		
		userRepo.save(u);
		repo.deleteById(id);
		return u;
	}
	
	public UserProfile removeLike(Long up, Long post) {
		UserProfile profilo = repo.findById(up).get();
		Post p = postRepo.findById(post).get();
		
		Set<Post> likesOld = profilo.getLikes();
		likesOld.removeIf(e -> e.getId().equals(p.getId()));
		p.setIsLiked(null);
		postRepo.save(p);
		repo.save(profilo);
		return profilo;
	}
	
	public Page<UserProfile> getAllUsersProfiles(Pageable p){
		return (Page<UserProfile>) repo.findAll(p);
	}
	
}
