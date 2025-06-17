package com.lcwd.electronic.store.Electronic.store.dtos;

import com.lcwd.electronic.store.Electronic.store.Entites.Order;
import com.lcwd.electronic.store.Electronic.store.Entites.Product;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.nio.Buffer;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderItemDto {
    private int orderItemId;

    private int quantity;

    private int totalPrice;


    private Productdtos product;


}
