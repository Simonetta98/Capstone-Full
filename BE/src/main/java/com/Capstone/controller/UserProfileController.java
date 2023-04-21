package com.Capstone.controller;

import java.util.List;
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

import com.Capstone.entity.Post;
import com.Capstone.entity.User;
import com.Capstone.entity.UserProfile;
import com.Capstone.exception.NotFoundEx;
import com.Capstone.exception.NotYetImplementedEx;
import com.Capstone.payload.PatchDto;
import com.Capstone.service.UserProfileService;
import com.Capstone.service.UserService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/userprofile")
public class UserProfileController {

	@Autowired UserProfileService service;
	@Autowired UserService us;


    @PostMapping("/for/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserProfile> create(@RequestBody UserProfile profilo, @PathVariable long id){
    	User u = us.getUserById(id);
    	profilo.setUser(u);
        service.createUserProfile(profilo);
        return new ResponseEntity<UserProfile>(profilo, HttpStatus.OK);
    }
    
    @PostMapping("/likes/{idProfile}/{idPost}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Post> createLike(@PathVariable long idProfile, @PathVariable long idPost){
        return new ResponseEntity<Post>(service.createLike(idProfile, idPost), HttpStatus.OK);
    }
    
    @GetMapping("/all/{page}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<UserProfile>> getAllUserProfiles(@PathVariable int page, @RequestParam(defaultValue = "10") int size) {
        Pageable sorting= PageRequest.of(page, size, Sort.by("id").ascending());
        return new ResponseEntity<Page<UserProfile>>(service.getAllUsersProfiles(sorting), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserProfile> getUserProfileById(@PathVariable long id){
        return new ResponseEntity<UserProfile>(service.getUserProfileById(id), HttpStatus.OK);
    }
    
    @GetMapping("/by/post/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserProfile> getUserProfileByPost(@PathVariable long id){
        return new ResponseEntity<UserProfile>(service.getProfileByPost(id), HttpStatus.OK);
    }
    
    
    @GetMapping("/posts/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Set<Post>> getUserProfilePosts(@PathVariable long id)throws NotFoundEx{
        return new ResponseEntity<Set<Post>>(service.getUserProfilePosts(id), HttpStatus.OK);
    	
    }
    
    @GetMapping("/username/{usr}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserProfile> getUserProfileByUsername(@PathVariable String usr){
        return new ResponseEntity<UserProfile>(service.getUserProfileByUsername(usr), HttpStatus.OK);
    }
    
    @GetMapping("/likes/{postId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UserProfile>> getUserProfilesThatLiked(@PathVariable long postId){
        return new ResponseEntity<List<UserProfile>>(service.getUserProfileByLikes(postId), HttpStatus.OK);
    }
    
    @GetMapping("/likes/posts/{profileId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Set<Post>> getUserProfileLikedPosts(@PathVariable long profileId){
        return new ResponseEntity<Set<Post>>(service.getUserProfileLikedPosts(profileId), HttpStatus.OK);
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
    
    @DeleteMapping ("/delete/{idProfile}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> deleteUserProfile(@PathVariable long idProfile){
        return new ResponseEntity<User>(service.removeUserProfile(idProfile), HttpStatus.OK);}
    
    @DeleteMapping ("/remove/like/{idProfile}/{idPost}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserProfile> deleteUserProfileLike(@PathVariable long idProfile, @PathVariable long idPost){
        return new ResponseEntity<UserProfile>(service.removeLike(idProfile, idPost), HttpStatus.OK);}
    
}
