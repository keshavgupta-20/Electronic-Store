package com.org.Electronic.store.controller;

import com.org.Electronic.store.dtos.*;

import com.org.Electronic.store.entites.Role;
import com.org.Electronic.store.repository.CategoryRepo;
import com.org.Electronic.store.repository.RoleRepo;
import com.org.Electronic.store.service.CategoryService;
import com.org.Electronic.store.service.OrderService;
import com.org.Electronic.store.service.ProdcutService;
import com.org.Electronic.store.service.UserServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/electrohub/admin")
public class AdminController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ProdcutService prodcutService;

    @Autowired
    private UserServices userServices;

    @Autowired
    private RoleRepo roleRepo;

    @GetMapping("/dashboard")
    public String dashboard(){
        return "dashboard";
    }


    @GetMapping("/products")
    public String products(Model model){
       List<ProductDto> product = new ArrayList<>();


        return "admin-product";
    }
    @GetMapping("/orders")
    public String  getOrders(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "4", required = false)int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "orderedDate", required = false)String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false)String sortDir, Model model){
        PageableResponse<OrderDto> orderDtos = orderService.getUser(pageNumber, pageSize, sortBy,  sortDir);
        model.addAttribute("orders", orderDtos.getContent());
        model.addAttribute("products",orderDtos.getContent());
        model.addAttribute("pageNumber");
        model.addAttribute("pageNumber", orderDtos.getPageNumber());
        model.addAttribute("totalPages", orderDtos.getTotalPages());
        model.addAttribute("pageSize", orderDtos.getPageSize());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("isLastPage", orderDtos.isLastPage());
        return "pending-orders";
    }

    @GetMapping("/inventry")
    public  String inventry(){
        return "inventry";
    }

    @GetMapping("/customer")
    public String totaluser(){
        return "show-users";
    }

    @GetMapping("/promotion")
    public String dealsOnProduct(){
        return "deal-edit";
    }

    @GetMapping("/adminview")
    public String adminView(){
        return "admin-view";
    }
    @GetMapping("/category/add-category")
    public String showAddCategoryForm(Model model) {
        CategoryDto category = new CategoryDto();
        model.addAttribute("category", category);
        return "add-category";
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDto>> getOrderOfUser(@PathVariable String userId){
        List<OrderDto> orderOfUser=  orderService.getOrderOfUser(userId);
        return new ResponseEntity<>(orderOfUser, HttpStatus.OK);
    }
    @PutMapping("/admin/{orderId}")
    public ResponseEntity<OrderDto> updateOrderByAdmin(@PathVariable String orderId, @RequestBody AdminUpdateOrder request) {
        return ResponseEntity.ok(orderService.updateOrderByAdmin(orderId, request));
    }

    @GetMapping("/view/{orderId}")
    public  String updateOrderDetail(@PathVariable("orderId") String orderId, Model model){
        OrderDto orderOfUser=  orderService.OrderByUser(orderId);
        model.addAttribute("order", orderOfUser);
        List<OrderItemDto> orderItemDto = orderService.orderByOrderItem(orderId);
        model.addAttribute("orderItem", orderItemDto);

       return "orderDetail";

    }

    @PostMapping("orders/update-status")
    public String updateOrderStatus(@RequestParam("orderId") String orderId,
                                    @RequestParam("orderStatus") String orderStatus,
                                    RedirectAttributes redirectAttributes) {
        try {
            // Update order status
            orderService.updateOrderStatus(orderId, orderStatus);

            redirectAttributes.addFlashAttribute("success", "Order status updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update order status.");
        }

        // Redirect back to the order details or order list page
        return "redirect:/electrohub/admin/orders";
    }

    @GetMapping("/show-admin")
    public String allAdmin(Model model){
        List<UserDto> userDtos = userServices.getAllAdmin();
        model.addAttribute("admins", userDtos);
        System.out.println(userDtos.get(0).getName());
        System.out.println(userDtos.get(0).getUserId());
        return "admin-view";
    }

    @GetMapping("/delete/{userId}")
    public String  deleteUser(Model model, @PathVariable("userId") String userId) throws IOException {
        userServices.deleteUser(userId);
        return "redirect:/electrohub/admin/show-admin";
    }

    @GetMapping("/add-admin")
    public  String addAdmin(Model model){
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "new-admin";
    }

    @PostMapping("/user/add")
    public String handleAdminAdd(@ModelAttribute("user") @Valid UserDto userDto,
                                 BindingResult result, Model model){
        userDto.setUserId(UUID.randomUUID().toString());
        Role normalRole = roleRepo.findByName("ROLE_ADMIN").orElse(null);
        userDto.setImageName("abc.jpg");
        if (result.hasErrors()) {
            return "add-admin";
        }
        userServices.createAdmin(userDto);
        return "redirect:/electrohub/admin/show-admin";


    }



}
