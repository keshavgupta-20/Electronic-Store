package com.yash.Electronic.store.repository;

import com.yash.Electronic.store.entites.Order;
import com.yash.Electronic.store.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderServiceRepo extends JpaRepository<Order, String> {
    List<Order> findByUser(User user);

}
