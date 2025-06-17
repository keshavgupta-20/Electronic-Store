package com.lcwd.electronic.store.Electronic.store.repositoreis;


import com.lcwd.electronic.store.Electronic.store.Entites.OrderItem;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepo extends JpaRepository<OrderItem, Integer> {

}
