package com.org.Electronic.store.repository;

import com.org.Electronic.store.entites.Order;
import com.org.Electronic.store.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderServiceRepo extends JpaRepository<Order, String> {
    List<Order> findByUser(User user);

}
