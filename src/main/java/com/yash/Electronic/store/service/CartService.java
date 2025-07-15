package com.yash.Electronic.store.service;

import com.yash.Electronic.store.dtos.AddItemToCartRequest;
import com.yash.Electronic.store.dtos.CartDto;
import org.springframework.stereotype.Component;

@Component
public interface CartService {


    CartDto addItemToCart(String userId, String productId, int quantity);

    //remove item from cart
    void removeItemFromCart(String userId, int cartItem);

    void clearCart(String userId);
    CartDto getCartByUser(String userId);
}
