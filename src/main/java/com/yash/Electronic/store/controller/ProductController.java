package com.yash.Electronic.store.controller;

import com.yash.Electronic.store.dtos.*;
import com.yash.Electronic.store.entites.Category;
import com.yash.Electronic.store.repository.CategoryRepo;
import com.yash.Electronic.store.service.CategoryService;
import com.yash.Electronic.store.service.FileService;
import com.yash.Electronic.store.service.ProdcutService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
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
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/ElectroHub/admin/product")
public class ProductController {
    @Autowired
    private ProdcutService prodcutService;
    private Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private FileService fileService;
    @Value("${product.profile.image.path}")
    private String imageUploadPath;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ModelMapper modelMapper;
    //create
    @PostMapping("/save")
    public String createProdcut(@Valid @ModelAttribute("product") ProductDto product, BindingResult result,
                                @RequestParam("productImage") MultipartFile coverImage, Model model) throws IOException {

        if (result.hasErrors() && product.getProductImage() != null){
            return "add-product";
        }
        if (coverImage.isEmpty() || !coverImage.getContentType().startsWith("image/")) {
            logger.info("Image is not update");
            model.addAttribute("imageError", "Please upload a valid image.");
            return "/ElectroHub/admin/category/";
        }

        String imageName = fileService.uploadFile(coverImage, imageUploadPath);
        product.setProductImage(imageName);
        ProductDto created = prodcutService.create(product);
        return "redirect:/ElectroHub/admin/product/show" ;
    }
    @GetMapping("/update")
    public String updateProduct(@ModelAttribute("product") @Valid ProductDto product,
                                @RequestParam("productImage") MultipartFile file,
                                BindingResult result,
                                Model model) throws IOException {
        if (!file.isEmpty()){
            String fullpath = imageUploadPath+product.getProductImage();
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
            product.setProductImage(filename);
        }
        ProductDto productDto = prodcutService.update(product, product.getProductId());
        return "redirect:/ElectroHub/admin/product/show" ;
    }
    @GetMapping("/delete/{productId}")
    public String delete(@PathVariable String productId) throws IOException {
        prodcutService.delete(productId);
        return "redirect:/ElectroHub/admin/product/show" ;
    }

    @GetMapping("/{productId}")
    public  ResponseEntity<ProductDto> getsingle(@PathVariable String productId){
        ProductDto productDto = prodcutService.getsingle(productId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }
    @GetMapping("/show")
    public  String getall(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                @RequestParam(value = "pageSize", defaultValue = "4", required = false)int pageSize,
                                                                @RequestParam(value = "sortBy", defaultValue = "title", required = false)String sortBy,
                                                                @RequestParam(value = "sortDir", defaultValue = "Asc", required = false)String sortDir, Model model){
    PageableResponse<ProductDto> pageableResponse = prodcutService.getAll(pageNumber, pageSize, sortBy, sortDir);
    model.addAttribute("product",pageableResponse.getContent());
    model.addAttribute("pageNumber");
        model.addAttribute("pageNumber", pageableResponse.getPageNumber());
        model.addAttribute("totalPages", pageableResponse.getTotalPages());
        model.addAttribute("pageSize", pageableResponse.getPageSize());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("isLastPage", pageableResponse.isLastPage());
        return "admin-product";
    }

    @GetMapping("/live")
    public  ResponseEntity<PageableResponse<ProductDto>> getAllLive(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                    @RequestParam(value = "pageSize", defaultValue = "4", required = false)int pageSize,
                                                                    @RequestParam(value = "sortBy", defaultValue = "title", required = false)String sortBy,
                                                                    @RequestParam(value = "sortDir", defaultValue = "Asc", required = false)String sortDir) {
        PageableResponse<ProductDto> pageableResponse = prodcutService.findByLiveTrue(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }
    @GetMapping("/search/{query}")
    public  ResponseEntity<PageableResponse<ProductDto>> SearchUSer(
            @PathVariable String query,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                     @RequestParam(value = "pageSize", defaultValue = "4", required = false)int pageSize,
                                                                     @RequestParam(value = "sortBy", defaultValue = "title", required = false)String sortBy,
                                                                     @RequestParam(value = "sortDir", defaultValue = "Asc", required = false)String sortDir) {
        PageableResponse<ProductDto> pageableResponse = prodcutService.searchByTitle(query, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }
    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadProductImage(@RequestParam("productImage")MultipartFile file, @PathVariable String productId) throws IOException {
        String imageName = fileService.uploadFile(file,imageUploadPath);
        ProductDto productDto = prodcutService.getsingle(productId);
        productDto.setProductImage(imageName);
        ProductDto productDto1 = prodcutService.update(productDto,productId);
        ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).success(true).httpStatus(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponse, HttpStatus.OK);
    }

    @GetMapping("/image/{productId}")
    public void servedProductImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
        ProductDto productDto = prodcutService.getsingle(productId);
        logger.info(productDto.getProductImage());
        InputStream inputStream = fileService.getResource(imageUploadPath, productDto.getProductImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(inputStream, response.getOutputStream());
    }
    @GetMapping("/add-product")
    public  String showUserForm(Model model){
        ProductDto product = new ProductDto();
        List<Category> categoryList = categoryRepo.findAll();
        List<CategoryDto> categoryDtos = categoryList.stream()
                .map(cat -> modelMapper.map(cat, CategoryDto.class))
                .collect(Collectors.toList());

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryDtos); // name matches the form

        return "add-product";
    }

    @GetMapping("/edit/{productId}")
    public String editPage(@PathVariable("productId")String productId, Model model){
        ProductDto productDto = prodcutService.getsingle(productId);
        List<Category> categoryList = categoryRepo.findAll();
//        fileService.getResource(imageUploadPath, );
        List<CategoryDto> categoryDto = categoryList.stream().map(cat-> modelMapper.map(cat, CategoryDto.class)).collect(Collectors.toList());
        model.addAttribute("category", categoryDto);
        model.addAttribute("product", productDto);
        return "product-edit";
    }


}
