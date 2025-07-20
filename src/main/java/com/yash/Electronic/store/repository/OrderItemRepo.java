package com.yash.Electronic.store.repository;


import com.yash.Electronic.store.entites.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepo extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findByOrderOrderId(String orderId);


}
