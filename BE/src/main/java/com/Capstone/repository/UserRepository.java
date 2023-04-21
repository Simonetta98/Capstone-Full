package com.Capstone.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Capstone.entity.User;
import com.Capstone.entity.UserProfile;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);
    
    User findByUserProfile(UserProfile userProfile);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
