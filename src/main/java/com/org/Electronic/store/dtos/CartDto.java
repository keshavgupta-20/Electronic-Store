package com.org.Electronic.store.dtos;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartDto {
    private String cartId;
    private Date createdAt;

    private List<CartItemDto> items = new ArrayList<>();
}
