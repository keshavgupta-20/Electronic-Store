package com.yash.Electronic.store.repository;


import com.yash.Electronic.store.entites.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepo extends JpaRepository<OrderItem, Integer> {

}
