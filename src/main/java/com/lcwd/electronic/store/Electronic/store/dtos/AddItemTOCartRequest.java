package com.lcwd.electronic.store.Electronic.store.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddItemTOCartRequest {
    private String productId;
    private int quantity;
}
