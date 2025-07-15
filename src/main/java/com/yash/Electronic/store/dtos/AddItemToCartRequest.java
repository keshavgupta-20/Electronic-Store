package com.yash.Electronic.store.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddItemToCartRequest {
    private String productId;
    private String userId;
    private int quantity;
}
