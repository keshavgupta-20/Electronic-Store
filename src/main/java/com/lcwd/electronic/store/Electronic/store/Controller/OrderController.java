package com.lcwd.electronic.store.Electronic.store.Controller;


import com.lcwd.electronic.store.Electronic.store.Service.OrderService;
import com.lcwd.electronic.store.Electronic.store.dtos.*;

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

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderRequest request){
        OrderDto orderDto = orderService.createOrder(request);
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    //remove
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResposeClass> deleteOrder(@PathVariable String orderId){
        orderService.removeOrder(orderId);
        ApiResposeClass resposeClass = ApiResposeClass.builder().status(HttpStatus.OK)
                .success(true)
                .message("Order is removed")
                .build();
        return new ResponseEntity<>(resposeClass, HttpStatus.OK);
    }
    //getOrderOfUser
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDto>> getOrderOfUser(@PathVariable String userId){
        List<OrderDto> orderOfUser=  orderService.getOrderOfUser(userId);
        return new ResponseEntity<>(orderOfUser, HttpStatus.OK);
    }
    //
    @GetMapping
    public ResponseEntity<PegeableResponse<OrderDto>>  getOrders(
                                                                 @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                 @RequestParam(value = "pageSize", defaultValue = "4", required = false)int pageSize,
                                                                 @RequestParam(value = "sortBy", defaultValue = "orderedDate", required = false)String sortBy,
                                                                 @RequestParam(value = "sortDir", defaultValue = "desc", required = false)String sortDir){
        PegeableResponse<OrderDto> orderDtos = orderService.getUser(pageNumber, pageSize, sortBy,  sortDir);
        return new ResponseEntity<>(orderDtos, HttpStatus.OK);
    }

    @PutMapping("/admin/{orderId}")
    public ResponseEntity<OrderDto> updateOrderByAdmin(@PathVariable String orderId, @RequestBody AdminUpdateOrder request) {
        return ResponseEntity.ok(orderService.updateOrderByAdmin(orderId, request));
    }

    @PutMapping("/user/{orderId}")
    public ResponseEntity<OrderDto> updateOrderByUser(@PathVariable String orderId, @RequestBody UserUpdateOrder request) {
        return ResponseEntity.ok(orderService.updateOrderByUser(orderId, request));
    }


}
