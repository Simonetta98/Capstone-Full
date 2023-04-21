package com.Capstone.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users_profile")
public class UserProfile {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private LocalDate birthdate;
	
	@Column(length = 900)
	private String description;
	
    @JsonBackReference // to avoid infinite recursion
    @OneToOne
    @JoinColumn(name="id_user")
    private User user;
    
    private String github;
    
    private String linkedin;
    
    @Enumerated(EnumType.STRING)
    private Gender gender;  
	
    @JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)  
	@JoinColumn(name = "id_profile")
	private Set<Badge> badge = new HashSet<>();
	 
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)  
	@JoinColumn(name = "id_profile")
	private Set<Post> posts = new HashSet<>();
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_likes",
	            joinColumns = @JoinColumn(name = "profile_id", referencedColumnName = "id"),
	            inverseJoinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id")
	    )
    private Set<Post> likes = new HashSet<>();

	@Override
	public String toString() {
		return "UserProfile [id=" + id + ", birthdate=" + birthdate + ", description=" + description + "]";
	}
	
	
	
}
