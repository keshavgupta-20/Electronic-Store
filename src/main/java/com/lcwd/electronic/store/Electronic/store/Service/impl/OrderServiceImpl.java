package com.lcwd.electronic.store.Electronic.store.Service.impl;
import com.lcwd.electronic.store.Electronic.store.Entites.*;
import com.lcwd.electronic.store.Electronic.store.dtos.*;

import com.lcwd.electronic.store.Electronic.store.Exception.BadApiRequest;
import com.lcwd.electronic.store.Electronic.store.Exception.ResourceNotFoundException;
import com.lcwd.electronic.store.Electronic.store.Helpers.helper;
import com.lcwd.electronic.store.Electronic.store.Service.OrderService;

import com.lcwd.electronic.store.Electronic.store.repositoreis.CartRepo;
import com.lcwd.electronic.store.Electronic.store.repositoreis.OrderItemRepo;
import com.lcwd.electronic.store.Electronic.store.repositoreis.OrderServiceRepo;
import com.lcwd.electronic.store.Electronic.store.repositoreis.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public  class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderItemRepo orderItemRepo;
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OrderServiceRepo orderServiceRepo;

    @Autowired
    private CartRepo cartRepo;



    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {
        String userId = orderDto.getUserId();
        String cartId = orderDto.getCartId();
        User user= userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User does not exist with given id"));
        Cart cart =  cartRepo.findById(cartId).orElseThrow(()-> new ResourceNotFoundException("User does not exist with given id"));
        List<CartItem> cartItems = cart.getItems();
        if (cartItems.size()<=0){
            throw new BadApiRequest("Invalid number of item in the cart");
        }
        //other checks
        Order order = Order.builder().billingName(orderDto.getBillingName())
                .billingPhone((orderDto.getBillingPhone()))
                .billingAddress(orderDto.getBillingAddress())
                .orderedDate(new Date())
                .deliveredDate(null)
                .paymentStatus(orderDto.getPaymentStatus())
                .orderStatus(orderDto.getOrderStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user).build();
        AtomicReference<Integer> orderAmount = new AtomicReference<>(0);
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
//            CartItem->OrderItem
            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice())
                    .order(order)
                    .build();

            orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        order.setOrderAmount(orderAmount.get());

        //
        cart.getItems().clear();
        cartRepo.save(cart);
        Order savedOrder = orderServiceRepo.save(order);
        return mapper.map(savedOrder, OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {

        Order order = orderServiceRepo.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("order is not found !!"));
        orderServiceRepo.delete(order);

    }

    @Override
    public List<OrderDto> getOrderOfUser(String userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found !!"));
        List<Order> orders = orderServiceRepo.findByUser(user);
        List<OrderDto> orderDtos = orders.stream().map(order -> mapper.map(order, OrderDto.class)).collect(Collectors.toList());
        return orderDtos;
    }


    @Override
    public PegeableResponse<OrderDto> getUser(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Order> page = orderServiceRepo.findAll(pageable);
        return helper.getPageableResponse(page, OrderDto.class);
    }


    @Override
    public OrderDto updateOrderByAdmin(String orderId, AdminUpdateOrder request) {
        Order order = orderServiceRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found !!"));

        order.setOrderStatus(request.getOrderStatus());
        order.setPaymentStatus(request.getPaymentStatus());
        order.setDeliveredDate(request.getDeliveredDate());

        Order updatedOrder = orderServiceRepo.save(order);
        return mapper.map(updatedOrder, OrderDto.class);
    }
    @Override
    public OrderDto updateOrderByUser(String orderId, UserUpdateOrder request) {
        Order order = orderServiceRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found !!"));

        order.setBillingName(request.getBillingName());
        order.setBillingPhone(request.getBillingPhone());
        order.setBillingAddress(request.getBillingAddress());

        Order updatedOrder = orderServiceRepo.save(order);
        return mapper.map(updatedOrder, OrderDto.class);
    }



}
