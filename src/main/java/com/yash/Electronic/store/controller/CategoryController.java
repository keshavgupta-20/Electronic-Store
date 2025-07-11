package com.yash.Electronic.store.controller;

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
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/categories")
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
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid  @RequestBody CategoryDto categoryDto){
        CategoryDto categoryDto1 = categoryService.create(categoryDto);
        return  new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);
    }
    //update
    @PutMapping("/{categoryId}")
    public  ResponseEntity<CategoryDto> updateCate(@Valid@PathVariable("categoryId") String categoryId, @RequestBody CategoryDto categoryDto){
        CategoryDto categoryDto1 = categoryService.update(categoryDto, categoryId);
        return  new ResponseEntity<>(categoryDto1, HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseClass> deleteUser(@PathVariable("categoryId") String categoryId){
        categoryService.delete(categoryId);
        ApiResponseClass apiResponseClass = ApiResponseClass.builder().message("Deleted SuccessFul").status(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(apiResponseClass, HttpStatus.OK);

    }

    //getsingle
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> CategorybyId(@PathVariable("categoryId") String  categoryId){
        CategoryDto categoryDto = categoryService.getSingle(categoryId);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    //getall
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getall(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "4", required = false)int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false)String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "Asc", required = false)String sortDir
    ){
       PageableResponse<CategoryDto> pageableResponse =  categoryService.getall(pageNumber, pageSize, sortBy, sortDir);
       return new ResponseEntity<>(pageableResponse, HttpStatus.OK);

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
    @GetMapping("/image/{categoryId}")
    public  void servedUserImage(@PathVariable String categoryId, HttpServletResponse response)throws IOException{
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
}
