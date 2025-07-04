package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.Entites.CartItem;
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

    private List<CartItem> items = new ArrayList<>();
}
