package com.yash.Electronic.store.controller;

import com.yash.Electronic.store.dtos.AddItemToCartRequest;
import com.yash.Electronic.store.dtos.CartDto;
import com.yash.Electronic.store.dtos.CartItemDto;
import com.yash.Electronic.store.exception.BadApiRequest;
import com.yash.Electronic.store.exception.ResourceNotFoundException;
import com.yash.Electronic.store.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody AddItemToCartRequest request) {
        System.out.println(request.getUserId());
        System.out.println(request.getProductId());
        System.out.println(request.getQuantity());
        System.out.println("hey");
        try {
            CartDto cartDto = cartService.addItemToCart(
                    request.getUserId(),
                    request.getProductId(),
                    request.getQuantity());

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "cart", cartDto,
                    "totalItems", cartDto.getItems().stream()
                            .mapToInt(CartItemDto::getQuantity)
                            .sum()
            ));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("status", "error", "message", e.getMessage()));
        } catch (BadApiRequest e) {
            return ResponseEntity.status(400).body(Map.of("status", "error", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("status", "error", "message", "Server error"));
        }

    }


}