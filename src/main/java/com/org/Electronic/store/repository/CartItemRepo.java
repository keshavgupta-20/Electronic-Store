package com.org.Electronic.store.repository;

import com.org.Electronic.store.entites.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepo extends JpaRepository<CartItem, Integer> {

//    List<CartItem> findAllByCartId(String cartId);

}
