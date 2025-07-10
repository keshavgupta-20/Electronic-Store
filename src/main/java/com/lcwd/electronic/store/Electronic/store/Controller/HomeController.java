package com.lcwd.electronic.store.Electronic.store.Controller;

import com.lcwd.electronic.store.Electronic.store.Entites.User;
import com.lcwd.electronic.store.Electronic.store.Helpers.LoginHelper;
import com.lcwd.electronic.store.Electronic.store.Service.UserServices;
import com.lcwd.electronic.store.Electronic.store.dtos.UserDto;
import jakarta.servlet.http.HttpSession;
import lombok.experimental.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/ElectroHub")
public class HomeController {

    @Autowired
    private UserServices userServices;



    @RequestMapping("/")
    public String LandingPage(Authentication authentication, HttpSession session){
//        System.out.println(principal.getName());
        String email = LoginHelper.getEmailOfLoggedInUser(authentication);
        if (email != null  && !email.isEmpty()){
            UserDto user = userServices.getUserbyEmail(email);

            boolean isAdmin = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
            if (isAdmin){
                session.setAttribute("userObj", user);
                return "redirect:/ElectroHub/admin/dashboard";
            }
            session.setAttribute("userObj", user);
        }

        return "LandingPage";
    }


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
        return "Register";
    }
    @RequestMapping("/deals")
    public String deals(){
        return "Deals";
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
