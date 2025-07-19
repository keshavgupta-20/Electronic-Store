package com.yash.Electronic.store.dtos;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartItemDto {
    private int cartItemId;
    private String productId;
    private String productName;
    private String productImage;
    private int quantity;
    private int price;
    private int discountedPrice;

    private int totalPrice;
}
