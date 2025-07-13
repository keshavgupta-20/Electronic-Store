package com.yash.Electronic.store.controller;

import com.yash.Electronic.store.dtos.CategoryDto;
import com.yash.Electronic.store.dtos.ProductDto;
import com.yash.Electronic.store.entites.Category;
import com.yash.Electronic.store.repository.CategoryRepo;
import com.yash.Electronic.store.service.CategoryService;
import com.yash.Electronic.store.service.ProdcutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ElectroHub/admin")
public class AdminController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ProdcutService prodcutService;

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
    public String orders(){
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



}
