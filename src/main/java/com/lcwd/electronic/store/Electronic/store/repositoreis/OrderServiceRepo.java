package com.lcwd.electronic.store.Electronic.store.repositoreis;

import com.lcwd.electronic.store.Electronic.store.Entites.Order;
import com.lcwd.electronic.store.Electronic.store.Entites.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderServiceRepo extends JpaRepository<Order, String> {
    List<User> findByUser(User user);

}
