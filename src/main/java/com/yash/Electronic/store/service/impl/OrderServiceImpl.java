package com.yash.Electronic.store.service.impl;



import com.yash.Electronic.store.entites.*;
import com.yash.Electronic.store.exception.BadApiRequest;
import com.yash.Electronic.store.exception.ResourceNotFoundException;
import com.yash.Electronic.store.helpers.Helper;
import com.yash.Electronic.store.repository.*;
import com.yash.Electronic.store.service.OrderService;


import com.yash.Electronic.store.dtos.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    private ContactDetailRepo contactDetailRepo;

    @Autowired
    private ProductRepo productRepo;


    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {
            String userId = orderDto.getUserId();
            String cartId = orderDto.getCartId();

            // Fetch user and cart
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User does not exist with given ID"));

            Cart cart = cartRepo.findById(cartId)
                    .orElseThrow(() -> new ResourceNotFoundException("Cart does not exist with given ID"));

            List<CartItem> cartItems = cart.getItems();
            if (cartItems == null || cartItems.isEmpty()) {
                throw new BadApiRequest("Cart has no items.");
            }

            // Create Order
            Order order = Order.builder()
                    .orderId(UUID.randomUUID().toString())
                    .billingName(orderDto.getBillingName())
                    .billingPhone(orderDto.getBillingPhone())
                    .billingAddress(orderDto.getBillingAddress())
                    .orderedDate(new Date())
                    .deliveredDate(null)
                    .paymentStatus(orderDto.getPaymentStatus())
                    .orderStatus(orderDto.getOrderStatus())
                    .user(user)
                    .build();

            AtomicReference<Integer> totalOrderAmount = new AtomicReference<>(0);

            List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
                Product product = cartItem.getProduct();
                int orderedQuantity = cartItem.getQuantity();

                // Reduce product quantity
                if (product.getQuantity() < orderedQuantity) {
                    throw new BadApiRequest("Product " + product.getTitle() + " does not have enough stock.");
                }

                product.setQuantity(product.getQuantity() - orderedQuantity);
                productRepo.save(product); // ✅ save updated product

                // Create OrderItem
                OrderItem orderItem = OrderItem.builder()
                        .product(product)
                        .quantity(orderedQuantity)
                        .totalPrice(orderedQuantity * product.getDiscountedPrice())
                        .order(order)
                        .build();

                totalOrderAmount.updateAndGet(amount -> amount + orderItem.getTotalPrice());
                return orderItem;
            }).collect(Collectors.toList());

            order.setOrderItems(orderItems);
            order.setOrderAmount(totalOrderAmount.get());

            // Clear the cart
            cart.getItems().clear();
            cartRepo.save(cart);

            // Save the order
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
    public PageableResponse<OrderDto> getUser(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Order> page = orderServiceRepo.findAll(pageable);
        return Helper.getPageableResponse(page, OrderDto.class);
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
    public void addAddressDetail(ContactDetailDto contactDetailDto){
        ContactDetail contactDetail= mapper.map(contactDetailDto, ContactDetail.class);
        contactDetailRepo.save(contactDetail);
    }

    public List<ContactDetailDto> addressDetailByUser(String userId){
        List<ContactDetail> contactDetails = contactDetailRepo.findByUserUserId(userId);
        List<ContactDetailDto> contactDetailDtoList = contactDetails.stream()
                .map(detail -> mapper.map(detail, ContactDetailDto.class))
                .toList();

        return contactDetailDtoList;
    }

    @Override
    public ContactDetailDto contactDetailById(String contactDetailId) {
        Optional<ContactDetail> contactDetail = contactDetailRepo.findById(contactDetailId);
        return mapper.map(contactDetail, ContactDetailDto.class);


    }





}
