package com.agusaliaga;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.agusaliaga.domain.ERole;
import com.agusaliaga.domain.Role;
import com.agusaliaga.repository.RoleRepository;

/*http://localhost:8080/swagger-ui.html*/

@SpringBootApplication
public class BootappApplication {
	
	@Autowired
	private RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(BootappApplication.class, args);
	}
	
	/**
	 * We need to create the Roles entries on the DB when the app starts, 
	 * as we are using a H2 in memory DB 
	 **/
	@Bean
	InitializingBean sendDatabase() {
		
		Role role1 = new Role(ERole.ROLE_USER);
		Role role2 = new Role(ERole.ROLE_MODERATOR);
		Role role3 = new Role(ERole.ROLE_ADMIN);
		
		return () -> {
			roleRepository.save(role1);
			roleRepository.save(role2);
			roleRepository.save(role3);
	     };
	}
}
