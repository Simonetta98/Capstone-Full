package com.Capstone.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Capstone.entity.User;
import com.Capstone.entity.UserProfile;
import com.Capstone.exception.NotFoundEx;
import com.Capstone.exception.NotYetImplementedEx;
import com.Capstone.payload.PatchDto;
import com.Capstone.service.UserService;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired UserService service;

	//Post attraverso auth
	
    @GetMapping("/all/{page}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<User>> getAllUsers(@PathVariable int page, @RequestParam(defaultValue = "10") int size) {
        Pageable sorting= PageRequest.of(page, size, Sort.by("id").ascending());
        return new ResponseEntity<Page<User>>(service.getAllUsers(sorting), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<User> getUserById(@PathVariable long id){
        return new ResponseEntity<User>(service.getUserById(id), HttpStatus.OK);
    }
    
    @GetMapping("profile/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserProfile> getUserProfileByUserId(@PathVariable long id){
        return new ResponseEntity<UserProfile>(service.getUserProfile(id), HttpStatus.OK);
    }
    
    @GetMapping("by/profile/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<User> getUserByUserProfileId(@PathVariable long id){
        return new ResponseEntity<User>(service.getUserByProfile(id), HttpStatus.OK);
    }
    
    @GetMapping("/username/{usr}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<User> getUserByUsername(@PathVariable String usr){
        return new ResponseEntity<User>(service.getByUsername(usr), HttpStatus.OK);
    }
   
    @GetMapping("/username/{usr}/email/{eml}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<User> getUserByUsernameOrEmail(@PathVariable String usr, @PathVariable String eml){
        return new ResponseEntity<User>(service.geyByUsernameOrEmail(usr, eml), HttpStatus.OK);
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable long id){
        return new ResponseEntity<String>(service.removeUser(id), HttpStatus.OK);}

}
