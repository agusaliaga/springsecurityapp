package com.agusaliaga.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.agusaliaga.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
	Optional<User> findUserByUserName(String userName);
	
	Boolean existsByUserName(String userName);

	Boolean existsByEmail(String email);
}
