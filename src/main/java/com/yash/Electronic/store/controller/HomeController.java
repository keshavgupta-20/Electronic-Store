package com.yash.Electronic.store.controller;

import com.yash.Electronic.store.dtos.*;
import com.yash.Electronic.store.helpers.LoginHelper;
import com.yash.Electronic.store.service.*;
import com.yash.Electronic.store.repository.UserRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/ElectroHub")
public class HomeController {

    @Autowired
    private UserServices userServices;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProdcutService prodcutService;

    @Autowired
    private FileService fileService;

    @Value("${category.profile.image.path}")
    private String imageUploadPath;

    @Autowired
    private CartService cartService;



    @RequestMapping("/")
    public String LandingPage(Authentication authentication, HttpSession session, Model model){
        List<CategoryDto> list = categoryService.getAllCategory();
        for (int i =0;i<list.size();i++){
            System.out.println(list.get(i));;
        }
        List<ProductDto> productDtos = prodcutService.findByLiveTrue();
        model.addAttribute("categories", list);
        model.addAttribute("products", productDtos);
        if (authentication != null) {
            String email = LoginHelper.getEmailOfLoggedInUser(authentication);
            if (email != null && !email.isEmpty()) {
                UserDto user = userServices.getUserbyEmail(email);


                boolean isAdmin = authentication.
                        getAuthorities().
                        stream().
                        anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
                session.setAttribute("userObj", user);
                session.setAttribute("isAdmin", isAdmin);
                if (isAdmin) {

                    return "redirect:/ElectroHub/admin/dashboard";
                }

            }
        }

        return "landing-Page";
    }

    @Autowired
    private UserRepo userRepo;



    @GetMapping("/products")
    public  String getalls(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                          @RequestParam(value = "pageSize", defaultValue = "10", required = false)int pageSize,
                          @RequestParam(value = "sortBy", defaultValue = "title", required = false)String sortBy,
                          @RequestParam(value = "sortDir", defaultValue = "Asc", required = false)String sortDir, Model model){
        PageableResponse<ProductDto> pageableResponse = prodcutService.getAll(pageNumber, pageSize, sortBy, sortDir);
        model.addAttribute("products",pageableResponse.getContent());
        model.addAttribute("pageNumber");
        model.addAttribute("pageNumber", pageableResponse.getPageNumber());
        model.addAttribute("totalPages", pageableResponse.getTotalPages());
        model.addAttribute("pageSize", pageableResponse.getPageSize());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("isLastPage", pageableResponse.isLastPage());
        return "product";
    }

    @RequestMapping("/login")
    public String loginPage(Model model,
                            @RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout) {

        if (error != null) {
            model.addAttribute("loginError", "Invalid email or password.");
        }

        if (logout != null) {
            model.addAttribute("loginError", "You have been successfully logged out.");
        }


        model.addAttribute("loginUser", new UserDto()); // or your login form object
        return "login";
    }

    @RequestMapping("/register")
    public String register(Model model){
        model.addAttribute("registerUser", new UserDto());
        return "register";
    }
    @RequestMapping("/deals")
    public String deals(){
        return "deals";
    }

    @RequestMapping("/cart")
    public String cart(){
        return "cart";
    }

    @RequestMapping("/wishlist")
    public String whislist(){
        return "wishlist";
    }
//    @PostMapping("cart/product/{productId}/user/{userId}")
//    public String addItemTocart(@PathVariable String userId, @PathVariable String productId){
//        CartDto cartDto = cartService.addItemToCart(userId, productId);
//        return ;
//    }



}
