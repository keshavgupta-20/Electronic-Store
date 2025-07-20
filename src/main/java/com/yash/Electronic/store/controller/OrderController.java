package com.yash.Electronic.store.controller;


import com.yash.Electronic.store.entites.Order;
import com.yash.Electronic.store.service.CartService;
import com.yash.Electronic.store.service.OrderService;



import com.yash.Electronic.store.dtos.*;
import com.yash.Electronic.store.service.UserServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.spring6.processor.SpringSrcTagProcessor;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Controller
@RequestMapping("/electrohub/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserServices userServices;
    //create


    //remove
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseClass> deleteOrder(@PathVariable String orderId){
        orderService.removeOrder(orderId);
        ApiResponseClass resposeClass = ApiResponseClass.builder()
                .success(true)
                .message("Order is removed")
                .build();
        return new ResponseEntity<>(resposeClass, HttpStatus.OK);
    }
    //getOrderOfUser

    //






    @GetMapping("/address/add/{userId}")
    public String orderAddress(Model model, @PathVariable String userId){

        ContactDetailDto contactDetailDto = new ContactDetailDto();
        contactDetailDto.setContactDetailId(userId);
        model.addAttribute("address", contactDetailDto);
        model.addAttribute("userId", userId);


        return "add-address";
    }

    @PostMapping("/address/save/{userId}")
    public String saveAddress(@Valid @ModelAttribute("address") ContactDetailDto contactDetailDto,
                              @PathVariable String userId,
                              BindingResult bindingResult,
                              Model model) {
        contactDetailDto.setUserId(userId);
        String contactId = UUID.randomUUID().toString();
        contactDetailDto.setContactDetailId(contactId);


        // If validation errors exist, return back to form
        if (bindingResult.hasErrors()) {
            model.addAttribute("address", contactDetailDto);
            return "add-address";  // Your form view name
        }
        List<ContactDetailDto> contactDetaillist = orderService.addressDetailByUser(userId);

        try {

            orderService.addAddressDetail(contactDetailDto);
            List<ContactDetailDto> contactDetailDtoList = orderService.addressDetailByUser(userId);
            model.addAttribute("addresses", contactDetailDtoList);

            return "redirect:/electrohub/cart/"+ contactDetailDto.getUserId();
        } catch (Exception e) {
            model.addAttribute("address", contactDetailDto);
            model.addAttribute("errorMessage", "Something went wrong while saving address.");
            return "add-address";  // Stay on form view with error
        }
    }

    @PostMapping("/checkout")
    public String proceedToCheckout(@RequestParam String userId,
                                    @RequestParam String cartId,
                                    @RequestParam String addressId,
                                    Model model) {


        model.addAttribute("userId", userId);


        CartDto cartDto = cartService.getCartByUser(userId);
        List<CartItemDto> items = cartDto.getItems();


        if (items == null || items.isEmpty()) {
            model.addAttribute("error", "Your cart is empty.");
            return "error-page"; // redirect to an error page or show message
        }

        // Debugging: check productName
        for (CartItemDto cartItemDto : items) {
            System.out.println("Product Name: " + cartItemDto.getProductName());
            System.out.println("Quantity: " + cartItemDto.getQuantity());
            System.out.println("Total Price: " + cartItemDto.getTotalPrice());
        }

        // Calculate total price
        int finalTotalPrice = items.stream()
                .mapToInt(CartItemDto::getTotalPrice)
                .sum();

        // Add cart and price details to model
        model.addAttribute("totalPrice", finalTotalPrice);
        model.addAttribute("cartItems", items);
        model.addAttribute("cartId", cartId);

        // Get and set contact address
        System.out.println("Address ID: " + addressId);
        ContactDetailDto contactDetailDto = orderService.contactDetailById(addressId);

        if (contactDetailDto == null) {
            model.addAttribute("error", "Address not found.");
            return "error-page";
        }

        model.addAttribute("address", contactDetailDto);
        model.addAttribute("user", userId);

        return "checkout-page";
    }

    @PostMapping("/place")
    public String placeOrder(@RequestParam String userId,
                             @RequestParam String cartId,
                             @RequestParam String addressId,
                             @RequestParam String paymentMethod,
                             @RequestParam int totalPrice
                             ) {
        UserDto userDto = userServices.getUser(userId);
        ContactDetailDto contactDetailDto = orderService.contactDetailById(addressId);
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setUserId(userId);
        createOrderRequest.setOrderStatus("Placed");
        createOrderRequest.setCartId(cartId);
        String userAddresss = contactDetailDto.getHouseNo() + "," + contactDetailDto.getStreet() +
                "," + contactDetailDto.getCity() +  "," + contactDetailDto.getCountry() + "," + contactDetailDto.getPincode();
        createOrderRequest.setBillingAddress(userAddresss);
        createOrderRequest.setBillingPhone(contactDetailDto.getContactNo());
        createOrderRequest.setBillingName(userDto.getName());
        createOrderRequest.setPaymentStatus(paymentMethod);
        createOrderRequest.setOrderAmount(totalPrice);
        orderService.createOrder(createOrderRequest);

        return "redirect:/electrohub/orders/success";
    }

    @GetMapping("/success")
    public String  sucecss(){
        return "success";
    }










}
