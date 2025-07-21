package com.yash.Electronic.store.controller;

import com.yash.Electronic.store.entites.Category;
import com.yash.Electronic.store.service.CategoryService;
import com.yash.Electronic.store.service.FileService;
import com.yash.Electronic.store.service.ProdcutService;



import com.yash.Electronic.store.dtos.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/electrohub/admin/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProdcutService prodcutService;

    @Autowired
    private FileService fileService;

    @Value("${category.profile.image.path}")
    private String imageUploadPath;
    private Logger logger = LoggerFactory.getLogger(CategoryController.class);
    //create
    @PostMapping("/add/save")
    public String createCategory(@Valid @ModelAttribute("category") CategoryDto category,
                                 BindingResult result, @RequestParam("coverPage") MultipartFile coverImages, Model model) throws IOException {


        if (coverImages.isEmpty() || !coverImages.getContentType().startsWith("image/")) {
            logger.info("Image is not update");
            model.addAttribute("imageError", "Please upload a valid image.");
            return "/electrohub/admin/category/";
        }
        String imageName = fileService.uploadFile(coverImages, imageUploadPath);

        category.setCoverPage(imageName);
        CategoryDto categoryDto =  categoryService.create(category);
        return "redirect:/electrohub/admin/category/show";
    }
    //update
    @PostMapping("/update")
    public  String updateCategory(@ModelAttribute("category") @Valid CategoryDto category,
                              @RequestParam("coverImage") MultipartFile file,
                              BindingResult result,
                              Model model) throws IOException {

        if (!file.isEmpty()){
            String fullpath = imageUploadPath+category.getCoverPage();
            Path path = Paths.get(fullpath);
            try{
                Files.delete(path);
            }
            catch (IOException ex){
                logger.info("User image not found in folder");
                ex.printStackTrace();
            }
            catch (Exception ex){
                ex.printStackTrace();
            }

            String filename = fileService.uploadFile(file, imageUploadPath);
            category.setCoverPage(filename);
        }

        CategoryDto categoryDto1 = categoryService.update(category, category.getCategoryId());
        return "redirect:/electrohub/admin/category/show";
    }

    //delete
    @GetMapping("/delete/{categoryId}")
    public String deleteUser(@PathVariable("categoryId") String categoryId){
        categoryService.delete(categoryId);
        ApiResponseClass apiResponseClass = ApiResponseClass.builder().message("Deleted SuccessFul").status(HttpStatus.OK).success(true).build();
        return "redirect:/electrohub/admin/category/show";

    }

    //getall
    @GetMapping("/show")
    public String getall(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false)int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false)String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "Asc", required = false)String sortDir, Model model
    ){
       PageableResponse<CategoryDto> pageableResponse =  categoryService.getall(pageNumber, pageSize, sortBy, sortDir);
       model.addAttribute(pageableResponse);
        model.addAttribute("categories", pageableResponse.getContent());
        model.addAttribute("pageNumber", pageableResponse.getPageNumber());
        model.addAttribute("totalPages", pageableResponse.getTotalPages());
        model.addAttribute("pageSize", pageableResponse.getPageSize());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("isLastPage", pageableResponse.isLastPage());
       return "admin/category";

    }
    @GetMapping("/edit/{categoryId}")
    public String showEditForm(@PathVariable String categoryId, Model model){
        CategoryDto categoryDto = categoryService.getSingle(categoryId);
        model.addAttribute("category",categoryDto);
        return "admin/edit-category";
    }
}
