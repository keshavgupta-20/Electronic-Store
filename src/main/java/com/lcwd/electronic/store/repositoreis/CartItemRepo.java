package com.lcwd.electronic.store.repositoreis;

import com.lcwd.electronic.store.Entites.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepo extends JpaRepository<CartItem, Integer> {

}
