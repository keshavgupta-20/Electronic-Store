package com.yash.Electronic.store.controller;


import com.yash.Electronic.store.service.OrderService;



import com.yash.Electronic.store.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
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


}
