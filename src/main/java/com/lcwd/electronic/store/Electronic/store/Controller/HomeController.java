package com.lcwd.electronic.store.Electronic.store.Controller;

import com.lcwd.electronic.store.Electronic.store.dtos.RegisterUser;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/ElectroHub")
public class HomeController {

    @RequestMapping("/")
    public String LandingPage(){
        return "LandingPage";
    }


    @RequestMapping("/productss")
    public String products(){
        return "product";
    }

    @RequestMapping("/login")
    public String Login(){
        return "Login";
    }
    @RequestMapping("/register")
    public String register(Model model){
        model.addAttribute("registerUser", new RegisterUser());
        return "Register";
    }
    @RequestMapping("/deals")
    public String deals(){
        return "Deals";
    }

}
