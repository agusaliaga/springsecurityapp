package com.agusaliaga.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.agusaliaga.domain.ERole;
import com.agusaliaga.domain.Role;

@Repository
public interface RoleRepository extends CrudRepository <Role, Long> {
	Optional<Role> findByName(ERole name);
}
