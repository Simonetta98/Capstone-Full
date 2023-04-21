package com.Capstone.runner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.Capstone.entity.Badge;
import com.Capstone.entity.Btype;
import com.Capstone.entity.Comment;
import com.Capstone.entity.Post;
import com.Capstone.entity.Ptype;
import com.Capstone.entity.User;
import com.Capstone.entity.UserProfile;
import com.Capstone.repository.PostRepository;
import com.Capstone.repository.RoleRepository;
import com.Capstone.service.AuthService;
import com.Capstone.service.PostService;
import com.Capstone.service.UserProfileService;
import com.Capstone.service.UserService;

@Component
public class CapRunner implements ApplicationRunner {
	
	@Autowired UserProfileService ups;
	@Autowired UserService us;
	@Autowired PostService ps;
	@Autowired AuthService authService;
	@Autowired RoleRepository roleRepository;
	@Autowired PostRepository postRepo;
	@Autowired PasswordEncoder passwordEncoder;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("CapstoneRunning...");
		
	    //User u = us.getUserById(2l);
		//System.out.println(u.getUserProfile());
		
		// PROFILI
		
		//gProfile(11l);
		
		// POSTS
		
		//gPost(13l);
		
		// COMMENTS
		
		//gComment(3l);
		
		// LIKES
		
		//gLike(8l, 6l);
		
		//removeLike(13l, 18l);
		
		//???????Post p = ps.getUserPostById(3l);  //query: dalla tabella user_likes prendi tutti gli utenti che hanno messo like al post(2l)
		
		// BADGES
		
		//gBadge(1l);
		
		//UserProfile up = ups.getUserProfileById(2l);
		//UserProfile up = ups.getUserProfileByUsername("admin");
		//System.out.println(up);
		
		//FIND LIKES IN POST
		//Post p = ps.getUserPostById(6l);
		// List<Post> posts = new ArrayList<Post>();
		// posts.add(p);
		//System.out.println(posts.size());
		//List<UserProfile> profiles = ups.getUserProfileByLikes(posts);
		//System.out.println(profiles.size());
		//UserProfile u = ups.getUserProfileById(13l);
		//System.out.println(u.getPosts());
	}
	
	
	public void gProfile(Long id) {  //id di uno User
		User u = us.getUserById(id);
		UserProfile p = new UserProfile();
		LocalDate d = LocalDate.parse("1998-04-07");
		p.setBirthdate(d);
		p.setDescription("Creator");
		p.setUser(u);
		
		ups.createUserProfile(p);
	}
	
	public void gPost(Long id) {     //id di uno UserProfile
		UserProfile up = ups.getUserProfileById(id);
		Post p = new Post();
		p.setTitle("Post di Simona");
		p.setDescription("Questo è un post di prova");
		p.setPType(Ptype.PUBLIC);
		
		//List<Post> posts = new ArrayList<Post>();
		//posts.add(p);
		
		up.getPosts().add(p);
		
		//up.setPosts(posts);
		ups.updateUserProfile(up);
	}
	
	public void gComment(Long id) {   //id di un Post
		Post p = ps.getUserPostById(id);
		UserProfile up = ups.getUserProfileById(2l);
		
		Comment c = new Comment();
		c.setText("commento prova");
		c.setUserProfile(up);
		
		Set<Comment> comments = new HashSet<Comment>();
		comments.add(c);
		p.setComments(comments);
		
		ps.updatePost(p);
	}
	
	public void gLike(Long up, Long post) {   //id di uno UserProfile e un post
		UserProfile profilo = ups.getUserProfileById(up);
		Post newLike = ps.getUserPostById(post);
		
		Set<Post> likesNew = new HashSet<Post>();
		
		Set<Post> likesOld = profilo.getLikes();
		likesOld.forEach(e-> likesNew.add(e));
		likesNew.add(newLike);
		
		//un Profilo(User) mette mi piace a dei post
		profilo.setLikes(likesNew);
		ups.updateUserProfile(profilo);
	}
	
	public void removeLike(Long up, Long post) {
		UserProfile profilo = ups.getUserProfileById(up);
		Post p = ps.getUserPostById(post);
		
		Set<Post> likesOld = profilo.getLikes();
		likesOld.removeIf(e -> e.getId().equals(p.getId()));
		
		ups.updateUserProfile(profilo);	
	}
	
	public void gBadge(Long id) {    //id di uno UserProfile
		UserProfile p = ups.getUserProfileById(id);
		Badge bg = new Badge();
		bg.setBType(Btype.OPINIONISTA);
		
		Set<Badge> bgs = new HashSet<Badge>();
		bgs.add(bg);
		
		p.setBadge(bgs);
		ups.updateUserProfile(p);
	}
	

}
