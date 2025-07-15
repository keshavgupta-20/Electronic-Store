package com.yash.Electronic.store.repository;

import com.yash.Electronic.store.dtos.CartItemDto;
import com.yash.Electronic.store.entites.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepo extends JpaRepository<CartItem, Integer> {

//    List<CartItem> findAllByCartId(String cartId);

}
