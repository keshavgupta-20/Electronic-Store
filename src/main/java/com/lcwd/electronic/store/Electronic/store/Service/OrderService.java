package com.lcwd.electronic.store.Electronic.store.Service;

import com.lcwd.electronic.store.Electronic.store.dtos.*;


import java.util.List;

public interface OrderService {
    //create order
    OrderDto createOrder(CreateOrderRequest request);

    void removeOrder(String orderId);

    //remove order

    //get order of user
    List<OrderDto> getOrderOfUser(String userId);
    //get order

    //order method(logic)related to user
    PegeableResponse<OrderDto> getUser(int pageNumber, int pageSize, String sortBy, String sortDir);
    OrderDto updateOrderByAdmin(String orderId, AdminUpdateOrder request);
    OrderDto updateOrderByUser(String orderId, UserUpdateOrder request);


}
