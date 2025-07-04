package com.lcwd.electronic.store.repositoreis;

import com.lcwd.electronic.store.Entites.Order;
import com.lcwd.electronic.store.Entites.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderServiceRepo extends JpaRepository<Order, String> {
    List<Order> findByUser(User user);

}
