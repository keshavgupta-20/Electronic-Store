package com.lcwd.electronic.store.Electronic.store.Service;

import com.lcwd.electronic.store.Electronic.store.dtos.AddItemTOCartRequest;
import com.lcwd.electronic.store.Electronic.store.dtos.CartDto;
import org.springframework.stereotype.Component;

@Component
public interface CartService {

    //add item to  cart
    //cart for user is not available
    CartDto addItemToCart(String userId, AddItemTOCartRequest request);

    //remove item from cart
    void removeItemFromCart(String userId, int cartItem);

    void clearCart(String userId);
    CartDto getCartByUser(String userId);
}
