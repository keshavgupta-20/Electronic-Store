package com.yash.Electronic.store.controller;

import com.yash.Electronic.store.dtos.*;
import com.yash.Electronic.store.entites.CartItem;
import com.yash.Electronic.store.helpers.LoginHelper;
import com.yash.Electronic.store.repository.CartItemRepo;
import com.yash.Electronic.store.service.*;
import com.yash.Electronic.store.repository.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/electrohub")
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

    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private OrderService orderService;

    @GetMapping("/search")
    public String SearchProduct(
            @RequestParam("q") String query,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "4", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "Asc", required = false) String sortDir,
            Model model,
            Authentication authentication,
            HttpSession session
    ) {
        PageableResponse<ProductDto> pageableResponse = prodcutService.searchByTitle(query, pageNumber, pageSize, sortBy, sortDir);

        model.addAttribute("products", pageableResponse.getContent());
        model.addAttribute("pageNumber", pageableResponse.getPageNumber()); // FIXED: Removed duplicate line
        model.addAttribute("totalPages", pageableResponse.getTotalPages());
        model.addAttribute("pageSize", pageableResponse.getPageSize());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("isLastPage", pageableResponse.isLastPage());

        if (authentication != null) {
            String email = LoginHelper.getEmailOfLoggedInUser(authentication);
            if (email != null && !email.isEmpty()) {
                UserDto userDto = userServices.getUserbyEmail(email);
                session.setAttribute("user", userDto);
            }
        }
        return "product";
    }


    @RequestMapping("/")
    public String LandingPage(Authentication authentication, HttpSession session, Model model, HttpServletRequest request){

        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
//        System.out.println(csrfToken);
        model.addAttribute("_csrf", csrfToken);

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
                session.setAttribute("user", user);
                session.setAttribute("isAdmin", isAdmin);
                if (isAdmin) {

                    return "redirect:/electrohub/admin/dashboard";
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
                           @RequestParam(value = "sortDir", defaultValue = "Asc", required = false)String sortDir, Model model,
                           Authentication authentication, HttpSession session,  HttpServletRequest request){
        PageableResponse<ProductDto> pageableResponse = prodcutService.getAll(pageNumber, pageSize, sortBy, sortDir);

        model.addAttribute("products",pageableResponse.getContent());
        model.addAttribute("pageNumber");
        model.addAttribute("pageNumber", pageableResponse.getPageNumber());
        model.addAttribute("totalPages", pageableResponse.getTotalPages());
        model.addAttribute("pageSize", pageableResponse.getPageSize());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("isLastPage", pageableResponse.isLastPage());
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
          System.out.println(csrfToken);
        model.addAttribute("_csrf", csrfToken);

        if (authentication != null){
            String email = LoginHelper.getEmailOfLoggedInUser(authentication);
            if (email != null  && !email.isEmpty()){
                UserDto userDto = userServices.getUserbyEmail(email);
                session.setAttribute("user", userDto);
            }
        }
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
    public String deals(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                        @RequestParam(value = "pageSize", defaultValue = "10", required = false)int pageSize,
                        @RequestParam(value = "sortBy", defaultValue = "title", required = false)String sortBy,
                        @RequestParam(value = "sortDir", defaultValue = "Asc", required = false)String sortDir, Model model){

        PageableResponse<ProductDto> pageableResponse = prodcutService.getProductDiscounted(pageNumber, pageSize, sortBy, sortDir);

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



    @GetMapping("/{categoryId}/products")
    public  String updateCategoryofProduct(@PathVariable("categoryId") String categoryId,
                                           @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                           @RequestParam(value = "pageSize", defaultValue = "4", required = false)int pageSize,
                                           @RequestParam(value = "sortBy", defaultValue = "title", required = false)String sortBy,
                                           @RequestParam(value = "sortDir", defaultValue = "Asc", required = false)String sortDir, Model model)
    {
        PageableResponse<ProductDto>  pageableResponse = prodcutService.getAllOfCategory(categoryId,pageNumber, pageSize, sortBy, sortDir);
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

    @GetMapping("/categories")
    public String allCategory(Model model){
        List<CategoryDto> list = categoryService.getAllCategory();
        model.addAttribute("categories", list);
        List<ProductDto> productDtos = prodcutService.findByLiveTrue();
        model.addAttribute("products", productDtos);


        return "user-category";
    }

    @RequestMapping("/wishlist")
    public String whislist(){
        return "wishlist";
    }

    @Autowired
    private ModelMapper mapper;
    @GetMapping("/cart/{userId}")
    public String getCart(@PathVariable String userId, Model model){
        CartDto cartDto = cartService.getCartByUser(userId);
        List<CartItemDto> item = cartDto.getItems();
        System.out.println(item.get(0).getDiscountedPrice());
        System.out.println(item.get(0).getPrice());
        System.out.println(item.get(0).getTotalPrice());

        int finalTotalPrice = item.stream()
                .mapToInt(CartItemDto::getTotalPrice)
                .sum();

        model.addAttribute("finalPrice",  finalTotalPrice);
        model.addAttribute("cartId",cartDto.getCartId());
        model.addAttribute("cart", item);
        model.addAttribute("user", userId);
        List<ContactDetailDto> contactDetailDto = new ArrayList<>();
        model.addAttribute("addresses", contactDetailDto);
        List<ContactDetailDto> contactDetailDtoList = orderService.addressDetailByUser(userId);
        model.addAttribute("addresses", contactDetailDtoList);


        return "cart";
    }
    @RequestMapping("/cart/{userId}/items/{itemId}")
    public String removeItemToCart(@PathVariable String userId, @PathVariable int itemId){
        cartService.removeItemFromCart(userId, itemId);
        return "redirect:/electrohub/cart/" + userId;
    }

    @RequestMapping("/cart/{userId}/clear")
    public String clearCart(@PathVariable String userId){
        cartService.clearCart(userId);
        return "redirect:/electrohub/cart/" + userId;
    }

    @GetMapping("/profile/{id}")
    public String profile(Model model, @PathVariable("id") String userId) {
        System.out.println(userId);
        UserDto userDto = userServices.getUser(userId);
        model.addAttribute("user", userDto);
        return "user-profile"; // inside templates/
    }

}
