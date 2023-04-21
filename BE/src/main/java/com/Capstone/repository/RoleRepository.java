package com.Capstone.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Capstone.entity.ERole;
import com.Capstone.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    
	Optional<Role> findByRoleName(ERole roleName);

}
