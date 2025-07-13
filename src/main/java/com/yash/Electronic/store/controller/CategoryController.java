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
@RequestMapping("/ElectroHub/admin/category")
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

        // Validate image (you can replace this with your @ImageValidator logic)
        if (coverImages.isEmpty() || !coverImages.getContentType().startsWith("image/")) {
            logger.info("Image is not update");
            model.addAttribute("imageError", "Please upload a valid image.");
            return "/ElectroHub/admin/category/";
        }
        String imageName = fileService.uploadFile(coverImages, imageUploadPath);

        category.setCoverPage(imageName);
        CategoryDto categoryDto =  categoryService.create(category);
        return "redirect:/ElectroHub/admin/category/show";
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
        return "redirect:/ElectroHub/admin/category/show";
    }

    //delete
    @GetMapping("/delete/{categoryId}")
    public String deleteUser(@PathVariable("categoryId") String categoryId){
        categoryService.delete(categoryId);
        ApiResponseClass apiResponseClass = ApiResponseClass.builder().message("Deleted SuccessFul").status(HttpStatus.OK).success(true).build();
        return "redirect:/ElectroHub/admin/category/show";

    }

    //getsingle
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> CategorybyId(@PathVariable("categoryId") String  categoryId){
        CategoryDto categoryDto = categoryService.getSingle(categoryId);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
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
       return "category";

    }
    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadUserFile(@Valid @RequestParam("categoryImage")MultipartFile image, @PathVariable String categoryId) throws IOException {
        String imageName = fileService.uploadFile(image, imageUploadPath);
        CategoryDto categoryDto = categoryService.getSingle(categoryId);
        categoryDto.setCoverPage(imageName);
        CategoryDto categoryDto1 = categoryService.update(categoryDto, categoryId);
        ImageResponse imageResponse = ImageResponse.builder().message("Image is inserted successfully").imageName(imageName).success(true).httpStatus(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    public  void servedUserImage( String categoryId, HttpServletResponse response)throws IOException{
        CategoryDto categoryDto = categoryService.getSingle(categoryId);
        logger.info(categoryDto.getCoverPage());
        InputStream inputStream = fileService.getResource(imageUploadPath, categoryDto.getCoverPage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(inputStream, response.getOutputStream());

    }
    //create productwithCategory
    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto>createProductWithCategroy(
            @PathVariable("categoryId") String categoryId, @RequestBody ProductDto productDto
    ){
     ProductDto productDto1 =    prodcutService.createwithCategory(productDto, categoryId);
        return new ResponseEntity<>(productDto1, HttpStatus.OK);
    }

    @PutMapping("/{categoryId}/products/{productId}")
    public  ResponseEntity<ProductDto> updateCategoryofProduct(
            @PathVariable("categoryId") String categoryId, @PathVariable("productId") String productId

            ){
        ProductDto productDto1 = prodcutService.updateCategory(productId, categoryId);
        return  new ResponseEntity<>(productDto1, HttpStatus.OK);

    }

    //getproductOfCategory
    @GetMapping("/{categoryId}/products")
    public  ResponseEntity<PageableResponse<ProductDto>> updateCategoryofProduct(@PathVariable("categoryId") String categoryId,
                                                                                 @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                                 @RequestParam(value = "pageSize", defaultValue = "4", required = false)int pageSize,
                                                                                 @RequestParam(value = "sortBy", defaultValue = "title", required = false)String sortBy,
                                                                                 @RequestParam(value = "sortDir", defaultValue = "Asc", required = false)String sortDir)
    {
        PageableResponse<ProductDto> productdtos1 = prodcutService.getAllOfCategory(categoryId,pageNumber, pageSize, sortBy, sortDir);
        return  new ResponseEntity<>(productdtos1, HttpStatus.OK);

    }
    @GetMapping("/edit/{categoryId}")
    public String showEditForm(@PathVariable String categoryId, Model model){
        CategoryDto categoryDto = categoryService.getSingle(categoryId);
        model.addAttribute("category",categoryDto);
        return "edit-category";
    }
}
