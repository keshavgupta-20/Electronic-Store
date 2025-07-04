package com.lcwd.electronic.store.repositoreis;


import com.lcwd.electronic.store.Entites.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepo extends JpaRepository<OrderItem, Integer> {

}
