package com.yash.Electronic.store.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ElectroHub/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public String dashboard(){
        return "dashboard";
    }

    @GetMapping("/category")
    public String categorys(){
        return "category";
    }
    @GetMapping("/products")
    public String products(){
        return "admin-product";
    }

    @GetMapping("/order")
    public String orders(){
        return "pending-orders";
    }

}
