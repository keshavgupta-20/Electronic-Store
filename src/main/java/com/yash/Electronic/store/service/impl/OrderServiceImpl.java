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
import org.springframework.transaction.annotation.Transactional;

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


    @Transactional
    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {
        String userId = orderDto.getUserId();
        String cartId = orderDto.getCartId();

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist"));

        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart does not exist"));

        List<CartItem> cartItems = cart.getItems();
        if (cartItems == null || cartItems.isEmpty()) {
            throw new BadApiRequest("Cart has no items.");
        }

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
                .orderAmount(orderDto.getOrderAmount()).build();

        AtomicReference<Integer> totalOrderAmount = new AtomicReference<>(0);

        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            int orderedQuantity = cartItem.getQuantity();

            if (product.getQuantity() < orderedQuantity) {
                throw new BadApiRequest("Product " + product.getTitle() + " does not have enough stock.");
            }

            product.setQuantity(product.getQuantity() - orderedQuantity);
            productRepo.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(orderedQuantity)
                    .totalPrice(orderedQuantity * product.getDiscountedPrice())
                    .order(order)
                    .build(); // ✅ Let JPA assign ID

            totalOrderAmount.updateAndGet(amount -> amount + orderItem.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        order.setOrderAmount(totalOrderAmount.get());

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
        if (contactDetail.isPresent()) {
            return mapper.map(contactDetail.get(), ContactDetailDto.class);
        } else {
            return null; // or throw an exception depending on your use case
        }
    }

    public OrderDto OrderByUser(String orderId) {
        return mapper.map(
                orderServiceRepo.findById(orderId)
                        .orElseThrow(() -> new ResourceNotFoundException("Order ID does not exist")),
                OrderDto.class
        );
    }
    public List<OrderItemDto> orderByOrderItem(String orderId){
        List<OrderItem> orderItem = orderItemRepo.findByOrderOrderId(orderId);
        List<OrderItemDto> orderItemDtos = orderItem.stream().map(orderItem1 -> mapper.map(orderItem1, OrderItemDto.class)).collect(Collectors.toList());
        return orderItemDtos;
    }

    @Override
    public void updateOrderStatus(String orderId, String newStatus) {
        // Fetch the order from DB
        Order order = orderServiceRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        // Update status
        order.setOrderStatus(newStatus);

        // Set delivered date if applicable
        if ("DELIVERED".equalsIgnoreCase(newStatus)) {
            order.setDeliveredDate(new Date());
        } else {
            order.setDeliveredDate(null); // Clear if not delivered
        }

        // Save back to DB
        orderServiceRepo.save(order);
    }









}
