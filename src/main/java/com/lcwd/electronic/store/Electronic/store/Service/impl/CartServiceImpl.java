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

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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
        int quantity = request.getQuantity();
        String  productId = request.getProductId();
        if (quantity <= 0){
            throw new BadApiRequest("Requested Entity is not Valid");
            
        }
       Product product =  productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Given id not found"));

       //fetch the user from the db
        User user =  userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User does not exist"));
        Cart cart = null;
       try{
           cart = cartRepo.findByUser(user).get();
       }
       catch (Exception e){
           cart = new Cart();
           cart.setCreatedAt(new Date());

       }
        //perform cart Operation

        AtomicReference<Boolean> updated = new AtomicReference<>(false);
        List<CartItem> itemList = cart.getItems();
      List<CartItem> updatedItem =  itemList.stream().map(itemLists-> {
           if (itemLists.getProduct().getProductId().equals(productId)){
               itemLists.setQuantity(quantity);
               itemLists.setTotalPrice(quantity * product.getPrice());
               updated.set(true);
           }
           return itemLists;
       }).collect(Collectors.toList());
       cart.setItems(updatedItem);

       //create item
        if (!updated.get()){
            CartItem cartItem = CartItem.builder().quantity(quantity).totalPrice(quantity * product.getPrice()).cart(cart).product(product).build();
            cart.getItems().add(cartItem);
        }
        cart.setUser(user);
        Cart updatedCart = cartRepo.save(cart);
        return modelMapper.map(updatedCart, CartDto.class);

    }

    @Override
    public void removeItemFromCart(String userId, int cartItem) {
        CartItem cartItem1 =  cartItemRepo.findById(cartItem).orElseThrow(() -> new ResourceNotFoundException("CartItem not found"));
        cartItemRepo.delete(cartItem1);
    }

    @Override
    public void clearCart(String userId) {

    }
}
