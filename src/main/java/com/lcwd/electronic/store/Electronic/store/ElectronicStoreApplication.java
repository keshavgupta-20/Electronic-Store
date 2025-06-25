package com.lcwd.electronic.store.Electronic.store;

import com.lcwd.electronic.store.Electronic.store.Entites.Role;
import com.lcwd.electronic.store.Electronic.store.repositoreis.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class ElectronicStoreApplication implements CommandLineRunner {

	@Autowired
	private RoleRepo roleRepo;
	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Role roleAdmin = roleRepo.findByName("ROlE_ADMIN").orElse(null);
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




	}
}
