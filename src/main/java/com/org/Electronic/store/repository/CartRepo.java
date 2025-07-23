package com.org.Electronic.store.repository;

import com.org.Electronic.store.entites.Cart;
import com.org.Electronic.store.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, String> {
    Optional<Cart> findByUser(User user);
}
