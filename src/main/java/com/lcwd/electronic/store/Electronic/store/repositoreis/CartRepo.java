package com.lcwd.electronic.store.Electronic.store.repositoreis;

import com.lcwd.electronic.store.Electronic.store.Entites.Cart;
import com.lcwd.electronic.store.Electronic.store.Entites.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, String> {
    Optional<Cart> findByUser(User user);
}
