package com.yash.Electronic.store.controller;


import com.yash.Electronic.store.entites.Order;
import com.yash.Electronic.store.service.CartService;
import com.yash.Electronic.store.service.OrderService;



import com.yash.Electronic.store.dtos.*;
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
@RequestMapping("/ElectroHub/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;
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
    @GetMapping
    public ResponseEntity<PageableResponse<OrderDto>>  getOrders(
                                                                 @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                 @RequestParam(value = "pageSize", defaultValue = "4", required = false)int pageSize,
                                                                 @RequestParam(value = "sortBy", defaultValue = "orderedDate", required = false)String sortBy,
                                                                 @RequestParam(value = "sortDir", defaultValue = "desc", required = false)String sortDir){
        PageableResponse<OrderDto> orderDtos = orderService.getUser(pageNumber, pageSize, sortBy,  sortDir);
        return new ResponseEntity<>(orderDtos, HttpStatus.OK);
    }



    @PutMapping("/user/{orderId}")
    public ResponseEntity<OrderDto> updateOrderByUser(@PathVariable String orderId, @RequestBody UserUpdateOrder request) {
        return ResponseEntity.ok(orderService.updateOrderByUser(orderId, request));
    }

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

            return "redirect:/ElectroHub/cart/"+ contactDetailDto.getUserId();
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
        List<CartItemDto> item = cartDto.getItems();

        int finalTotalPrice = item.stream()
                .mapToInt(CartItemDto::getTotalPrice)
                .sum();

        model.addAttribute("totalPrice",  finalTotalPrice);
        model.addAttribute("cartItems", item);
        model.addAttribute("cartId", cartId);
        ContactDetailDto contactDetailDto = orderService.contactDetailById(addressId);
        model.addAttribute("address", addressId);
        model.addAttribute("user", userId);
        return "checkout-page"; }
    @PostMapping("/ElectroHub/orders/place")
    public String placeOrder(@RequestParam String userId,
                             @RequestParam String cartId,
                             @RequestParam String addressId,
                             @RequestParam String paymentMethod,
                             @RequestParam double totalPrice
                             ) {
        ContactDetailDto contactDetailDto = orderService.contactDetailById(addressId);
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setUserId(userId);
        createOrderRequest.setOrderStatus("Placed");
        createOrderRequest.setCartId(cartId);
        String userAddresss = contactDetailDto.getHouseNo() + "," + contactDetailDto.getStreet() +
                "," + contactDetailDto.getCity() +  "," + contactDetailDto.getCountry() + "," + contactDetailDto.getPincode();
        createOrderRequest.setBillingPhone(userAddresss);
        createOrderRequest.setPaymentStatus(paymentMethod);
        System.out.println(userAddresss);
        return "redirect:/ElectroHub/orders/success";
    }







}
