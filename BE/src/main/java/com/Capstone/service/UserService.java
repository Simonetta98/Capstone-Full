package com.Capstone.service;


import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.Capstone.entity.Post;
import com.Capstone.entity.User;
import com.Capstone.entity.UserProfile;
import com.Capstone.exception.NotFoundEx;
import com.Capstone.repository.PostRepository;
import com.Capstone.repository.UserProfileRepository;
import com.Capstone.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

	@Autowired UserRepository repo;
	@Autowired UserProfileService upSrv;
	@Autowired PostRepository postRepo;
	Logger log = LoggerFactory.getLogger(UserService.class);
	
	public User createUser(User d) {
		repo.save(d);
		return d;
	}
	
	public User getUserById(Long id) {
		if(!repo.existsById(id)) {
			throw new EntityNotFoundException("Questo Utente non esiste");
		}
		User u = repo.findById(id).get();
		return u;
	}
	
	public User getByUsername(String username) {
		if(!repo.existsByUsername(username)) {
			throw new EntityNotFoundException("Utente non trovato");
		}
		User u = repo.findByUsername(username).get();
		return u;
	}
	
	public User geyByUsernameOrEmail(String username, String email) {
		if(!repo.existsByUsername(username) || !repo.existsByEmail(email)) {
			throw new EntityNotFoundException("Utente non trovato");
		}
		User u = repo.findByUsernameOrEmail(username, email).get();
		return u;
	}
	
	public User getUserByProfile(long idProfile) {
		UserProfile up = upSrv.getUserProfileById(idProfile);
		User u = repo.findByUserProfile(up);
		return u;
	}
	
	public UserProfile getUserProfile(long id) {
		if(!repo.existsById(id)) {
			throw new EntityNotFoundException("Questo Utente non esiste");
		}
		User u = repo.findById(id).get();
		UserProfile p = u.getUserProfile();
		return p;
	}
	
	public User updateUser(User d) {
		if(!repo.existsById(d.getId())) {
			throw new EntityNotFoundException("Questo Utente non esiste");
		}
		repo.save(d);
		return d;
	}
	
	//PATCH
	public boolean partialUpdate(long id, String key, String value)
		      throws NotFoundEx {
		    log.info("Search id={}", id);
		    Optional<User> optional = repo.findById(id);
		    if (optional.isPresent()) {
		      User user = optional.get();

		      if (key.equalsIgnoreCase("name")) {
		        log.info("Updating name");
		        user.setName(value);
		      }
		      if (key.equalsIgnoreCase("username")) {
		        log.info("Updating username");
		        user.setUsername(value);
		      }
		      if (key.equalsIgnoreCase("email")) {
			        log.info("Updating email");
			        user.setEmail(value);
			      }

		      repo.save(user);
		      return true;
		    } else {
		      throw new NotFoundEx("RESOURCE_NOT_FOUND");
		    }
		  }
	
	public String removeUser(Long id) {
		if(!repo.existsById(id)) {
			throw new EntityNotFoundException("Questo Utente non esiste");
		}
		repo.deleteById(id);
		return "Utente eliminato";
	}
	
	public Page<User> getAllUsers(Pageable pageable){
		return (Page<User>) repo.findAll(pageable);
	}
	
}
