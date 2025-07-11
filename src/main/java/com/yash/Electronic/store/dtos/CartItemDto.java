package com.yash.Electronic.store.dtos;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartItemDto {
    private int cartItemId;

    private ProductDto product;
    private int quantity;
    private int totalPrice;
}
