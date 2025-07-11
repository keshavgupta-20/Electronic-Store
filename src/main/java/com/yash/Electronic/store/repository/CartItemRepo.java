package com.yash.Electronic.store.repository;

import com.yash.Electronic.store.entites.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepo extends JpaRepository<CartItem, Integer> {

}
