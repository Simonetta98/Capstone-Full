package com.Capstone.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.Capstone.entity.Post;
import com.Capstone.entity.User;
import com.Capstone.entity.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
	
	public List<UserProfile> findByUser(User user);
	
	@Query("SELECT u FROM UserProfile u WHERE u.user.username = ?1")
	public Optional<UserProfile> findByUsername(String username);
	
	public UserProfile findByPosts(Post p);
	
	Boolean existsByUserUsername(String username);
	
	public List<UserProfile> findByLikesIn(List<Post> likes);
	
	public List<UserProfile> findByPostsIn(List<Post> posts);
}
