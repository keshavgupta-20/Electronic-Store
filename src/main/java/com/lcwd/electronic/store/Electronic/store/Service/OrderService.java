package com.lcwd.electronic.store.Electronic.store.Service;

import com.lcwd.electronic.store.Electronic.store.Entites.Order;
import com.lcwd.electronic.store.Electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.Electronic.store.dtos.PegeableResponse;

import javax.management.OperationsException;
import java.awt.print.Pageable;
import java.util.List;

public interface OrderService {
    //create order
    OrderDto createOrder(OrderDto orderDto, String userId, String cartId);

    void removeOrder(String orderId);

    //remove order

    //get order of user
    List<OrderDto> getOrderOfUser(String userId);
    //get order

    //order method(logic)related to user
    PegeableResponse<OrderDto> getUser(int pageNumber, int pageSize, String sortBy, String sortDir);


}
