package com.lcwd.electronic.store.Electronic.store.repositoreis;

import com.lcwd.electronic.store.Electronic.store.Entites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

public interface UserRepo  extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    List<User> findByNameContaining(String name);
}
