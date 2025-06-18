package com.lcwd.electronic.store.Electronic.store.Service.impl;

import com.lcwd.electronic.store.Electronic.store.Entites.Cart;
import com.lcwd.electronic.store.Electronic.store.Entites.CartItem;
import com.lcwd.electronic.store.Electronic.store.Entites.Product;
import com.lcwd.electronic.store.Electronic.store.Entites.User;
import com.lcwd.electronic.store.Electronic.store.Exception.BadApiRequest;
import com.lcwd.electronic.store.Electronic.store.Exception.ResourceNotFoundException;
import com.lcwd.electronic.store.Electronic.store.Service.CartService;
import com.lcwd.electronic.store.Electronic.store.dtos.AddItemTOCartRequest;
import com.lcwd.electronic.store.Electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.Electronic.store.repositoreis.CartItemRepo;
import com.lcwd.electronic.store.Electronic.store.repositoreis.CartRepo;
import com.lcwd.electronic.store.Electronic.store.repositoreis.ProductRepo;
import com.lcwd.electronic.store.Electronic.store.repositoreis.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
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
    public CartDto addItemToCart(String userId, AddItemTOCartRequest request) {
        String cartId = UUID.randomUUID().toString();

        int quantity = request.getQuantity();
        String productId = request.getProductId();

        if (quantity <= 0) {
            throw new BadApiRequest("Requested quantity is not valid");
        }

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Given product ID not found"));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist"));

        Cart cart = cartRepo.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setCartId(cartId);
            newCart.setCreatedAt(new Date());
            newCart.setUser(user);
            newCart.setItems(new ArrayList<>());
            return newCart;
        });

        AtomicReference<Boolean> updated = new AtomicReference<>(false);

        List<CartItem> itemList = cart.getItems();

       itemList = itemList.stream().map(item -> {
            if (item.getProduct().getProductId().equals(productId)) {
                item.setQuantity(quantity);
                item.setTotalPrice(quantity * product.getPrice());
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());

//        cart.setItems(updatedItem);


        if (!updated.get()) {
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getPrice())
                    .cart(cart)
                    .product(product)
                    .build();
            cart.getItems().add(cartItem);
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
        User user =  userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User does not exist"));
        Cart cart = cartRepo.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("User does not exist"));
        return modelMapper.map(cart, CartDto.class);
    }
}
