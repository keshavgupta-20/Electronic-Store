package com.yash.Electronic.store.service.impl;

import com.yash.Electronic.store.dtos.CartItemDto;
import com.yash.Electronic.store.entites.Cart;
import com.yash.Electronic.store.entites.CartItem;
import com.yash.Electronic.store.entites.Product;
import com.yash.Electronic.store.entites.User;
import com.yash.Electronic.store.exception.BadApiRequest;
import com.yash.Electronic.store.exception.ResourceNotFoundException;
import com.yash.Electronic.store.service.CartService;
import com.yash.Electronic.store.dtos.CartDto;

import com.yash.Electronic.store.repository.CartItemRepo;
import com.yash.Electronic.store.repository.CartRepo;
import com.yash.Electronic.store.repository.ProductRepo;
import com.yash.Electronic.store.repository.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CartDto addItemToCart(String userId, String productId, int quantity) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (quantity <= 0 || quantity > product.getQuantity()) {
            throw new BadApiRequest("Invalid quantity");
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepo.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setCartId(UUID.randomUUID().toString());
            newCart.setCreatedAt(new Date());
            newCart.setUser(user);
            newCart.setItems(new ArrayList<>());
            return cartRepo.save(newCart);
        });

        boolean found = false;
        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getProductId().equals(productId)) {

                int updatedQty = item.getQuantity() + quantity;
                item.setQuantity(updatedQty);
                item.setTotalPrice(updatedQty * product.getDiscountedPrice());
                found = true;
                break;
            }
        }

        if (!found) {
            CartItem newItem = CartItem.builder()
                    .product(product)
                    .quantity(quantity)
                    .totalPrice(quantity * product.getDiscountedPrice())
                    .cart(cart)
                    .build();
            cart.getItems().add(newItem);
        }

        Cart updatedCart = cartRepo.save(cart);
        return modelMapper.map(updatedCart, CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItemId) {
        CartItem cartItem = cartItemRepo.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem not found"));

        Cart cart = cartItem.getCart();
        if (cart == null || !cart.getUser().getUserId().equals(userId)) {
            throw new BadApiRequest("Item does not belong to this user's cart.");
        }

        cart.getItems().removeIf(item -> item.getCartItemId() == cartItemId);
        cartRepo.save(cart); // this triggers orphanRemoval and deletes the cart item
    }


    @Override
    public void clearCart(String userId) {
        User user =  userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User does not exist"));
        Cart cart = cartRepo.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("User does not exist"));
        cart.getItems().clear();
        cartRepo.save(cart);
    }

    @Override
    public CartDto getCartByUser(String userId) {
        // Fetch user
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist"));

        // Fetch cart for user
        Cart cart = cartRepo.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart does not exist"));

        // Create CartDto
        CartDto cartDto = new CartDto();
        cartDto.setCartId(cart.getCartId());
        cartDto.setCreatedAt(cart.getCreatedAt());

        // Convert each CartItem to CartItemDto manually
        List<CartItemDto> cartItemDtos = cart.getItems().stream().map(cartItem -> {
            CartItemDto dto = new CartItemDto();

            Product product = cartItem.getProduct();
            if (product != null) {
                dto.setProductId(product.getProductId());
                dto.setProductName(product.getTitle());
                dto.setProductImage(product.getProductImage());
                dto.setPrice(product.getPrice());
                dto.setDiscountedPrice(product.getDiscountedPrice());
            }

            dto.setCartItemId(cartItem.getCartItemId());
            dto.setQuantity(cartItem.getQuantity());

            // Calculate total price using discounted price
            dto.setTotalPrice(dto.getDiscountedPrice() * dto.getQuantity());

            return dto;
        }).collect(Collectors.toList());

        cartDto.setItems(cartItemDtos);
        return cartDto;
    }


}
