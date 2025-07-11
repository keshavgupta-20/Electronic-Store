package com.yash.Electronic.store.controller;

import com.yash.Electronic.store.helpers.LoginHelper;
import com.yash.Electronic.store.service.UserServices;
import com.yash.Electronic.store.dtos.UserDto;
import com.yash.Electronic.store.repository.UserRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/ElectroHub")
public class HomeController {

    @Autowired
    private UserServices userServices;



    @RequestMapping("/")
    public String LandingPage(Authentication authentication, HttpSession session){
//        System.out.println(principal.getName());
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



    @RequestMapping("/productss")
    public String products(){
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

}
