package com.lcwd.electronic.store.Electronic.store.dtos;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartItemDto {
    private int cartItemId;

    private Productdtos product;
    private int quantity;
    private int totalPrice;
}
