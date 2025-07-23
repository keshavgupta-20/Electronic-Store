package com.org.Electronic.store.repository;


import com.org.Electronic.store.entites.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepo extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findByOrderOrderId(String orderId);


}
