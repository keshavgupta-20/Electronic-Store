package com.yash.Electronic.store.repository;

import com.yash.Electronic.store.entites.Cart;
import com.yash.Electronic.store.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, String> {
    Optional<Cart> findByUser(User user);
}
