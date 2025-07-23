package com.org.Electronic.store;

import com.org.Electronic.store.entites.Role;
import com.org.Electronic.store.entites.User;
import com.org.Electronic.store.repository.RoleRepo;
import com.org.Electronic.store.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class ElectronicStoreApplication implements CommandLineRunner {


	@Autowired
	private UserRepo userRepo;
	@Autowired
	private RoleRepo roleRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Role roleAdmin = roleRepo.findByName("ROLE_ADMIN").orElse(null);
		if (roleAdmin == null) {
			Role role = new Role();
			role.setRoleId(UUID.randomUUID().toString());
			role.setName("ROLE_ADMIN");
			roleRepo.save(role);
		}
		Role roleUser = roleRepo.findByName("ROLE_USER").orElse(null);
		if (roleUser == null){
			Role role1 = new Role();
			role1.setRoleId(UUID.randomUUID().toString());
			role1.setName("ROLE_USER");
			roleRepo.save( role1);
		}

		User user = userRepo.findByEmail("Keshav@gmail.com").orElse(null);
		if (user == null){
			user = new User();
			user.setName("Keshav");
			user.setEmail("Keshav@gmail.com");
			user.setPassword(passwordEncoder.encode("Keshav@123"));
			user.setRoles(List.of(roleAdmin));
			user.setUserId(UUID.randomUUID().toString());
			user.setGender("Male");
			user.setAbout("Hey i am Admin");
			userRepo.save(user);
		}
	}
}
