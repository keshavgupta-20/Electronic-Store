package com.lcwd.electronic.store.Electronic.store;

import com.lcwd.electronic.store.Electronic.store.Entites.User;
import com.lcwd.electronic.store.Electronic.store.repositoreis.UserRepo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ElectronicStoreApplicationTests {
	@Autowired
	private UserRepo userRepo;


	@Test
	void contextLoads() {
	}

	@Test
	void Testing(){
		System.out.println("Helo");
	}
}
