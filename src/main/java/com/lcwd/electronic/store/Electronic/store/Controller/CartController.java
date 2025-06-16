package com.lcwd.electronic.store.Electronic.store.Controller;

import com.lcwd.electronic.store.Electronic.store.Service.CartService;
import com.lcwd.electronic.store.Electronic.store.dtos.AddItemTOCartRequest;
import com.lcwd.electronic.store.Electronic.store.dtos.ApiResposeClass;
import com.lcwd.electronic.store.Electronic.store.dtos.CartDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {
    @Autowired
    private CartService cart;

    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemTocart(@PathVariable String userId, @RequestBody AddItemTOCartRequest addItemTOCartRequest){
        CartDto cartDto = cart.addItemToCart(userId,addItemTOCartRequest);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }
    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<ApiResposeClass> removeItemToCart(@PathVariable String userId, @PathVariable int itemId){
        cart.removeItemFromCart(userId, itemId);
        ApiResposeClass resposeClass = ApiResposeClass.builder().message("Item is removed !!").success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(resposeClass, HttpStatus.OK);
    }

    //clearcart
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResposeClass> clearCart(@PathVariable String userId){
        cart.clearCart(userId);
        ApiResposeClass respose = ApiResposeClass.builder().message("Cart is clear").success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(respose, HttpStatus.OK);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCart(@PathVariable String userId){
        CartDto cartDto = cart.getCartByUser(userId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }
}
