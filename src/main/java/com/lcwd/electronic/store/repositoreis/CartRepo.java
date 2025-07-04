package com.lcwd.electronic.store.repositoreis;

import com.lcwd.electronic.store.Entites.Cart;
import com.lcwd.electronic.store.Entites.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, String> {
    Optional<Cart> findByUser(User user);
}
