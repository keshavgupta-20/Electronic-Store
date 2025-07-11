package com.yash.Electronic.store.controller;

import com.yash.Electronic.store.service.FileService;
import com.yash.Electronic.store.service.ProdcutService;
import com.yash.Electronic.store.dtos.ApiResposeClass;
import com.yash.Electronic.store.dtos.ImageResponse;
import com.yash.Electronic.store.dtos.PageableResponse;
import com.yash.Electronic.store.dtos.ProductDto;
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
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProdcutService prodcutService;
    private Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private FileService fileService;
    @Value("${product.profile.image.path}")
    private String imageUploadPath;
    //create
    @PostMapping
    public ResponseEntity<ProductDto> createProdcut(@Valid @RequestBody ProductDto productDto){
        ProductDto created = prodcutService.create(productDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@Valid @PathVariable String productId, @RequestBody ProductDto productDto){
        ProductDto updated = prodcutService.update(productDto, productId);
        return new ResponseEntity<>(updated, HttpStatus.OK);

    }
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResposeClass> delete(@PathVariable String productId) throws IOException {
        prodcutService.delete(productId);
        ApiResposeClass build = ApiResposeClass.builder().message("Product id deleted").status(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(build, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public  ResponseEntity<ProductDto> getsingle(@PathVariable String productId){
        ProductDto productDto = prodcutService.getsingle(productId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }
    @GetMapping
    public  ResponseEntity<PageableResponse<ProductDto>> getall(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                @RequestParam(value = "pageSize", defaultValue = "4", required = false)int pageSize,
                                                                @RequestParam(value = "sortBy", defaultValue = "title", required = false)String sortBy,
                                                                @RequestParam(value = "sortDir", defaultValue = "Asc", required = false)String sortDir){
    PageableResponse<ProductDto> pageableResponse = prodcutService.getAll(pageNumber, pageSize, sortBy, sortDir);
    return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
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

}
