package com.lcwd.electronic.store.Electronic.store.repositoreis;

import com.lcwd.electronic.store.Electronic.store.Entites.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepo extends JpaRepository<CartItem, Integer> {

}
