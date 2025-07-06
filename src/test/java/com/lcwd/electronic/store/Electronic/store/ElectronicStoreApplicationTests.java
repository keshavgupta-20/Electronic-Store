package com.lcwd.electronic.store.Electronic.store;

import com.lcwd.electronic.store.Electronic.store.Entites.User;
import com.lcwd.electronic.store.Electronic.store.repositoreis.UserRepo;
import com.lcwd.electronic.store.Electronic.store.security.JwtHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class ElectronicStoreApplicationTests {
	@Autowired
	private UserRepo userRepo;

	@Autowired
	private JwtHelper jwtHelper;
	@Test
	void contextLoads() {
	}
	@Test
	void testToken(){
		User user = userRepo.findByEmail("keshav@gmail.com").get();
		String token = jwtHelper.generateToken(user);
		System.out.println(token);
		System.out.println("Testing JWT token");
		System.out.println(jwtHelper.getUserFromToken(token));
		System.out.println(jwtHelper.isTokenExpired(token));
		System.out.println(jwtHelper.getExpirationDateFromToken(token));
//		System.out.println("Testing jwt Token");
		System.out.println();
	}
}
