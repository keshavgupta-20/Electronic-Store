package com.lcwd.electronic.store.Electronic.store.Controller;

import com.lcwd.electronic.store.Electronic.store.Service.FileService;
import com.lcwd.electronic.store.Electronic.store.Service.ProdcutService;
import com.lcwd.electronic.store.Electronic.store.dtos.ApiResposeClass;
import com.lcwd.electronic.store.Electronic.store.dtos.ImageResponse;
import com.lcwd.electronic.store.Electronic.store.dtos.PegeableResponse;
import com.lcwd.electronic.store.Electronic.store.dtos.Productdtos;
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
    public ResponseEntity<Productdtos> createProdcut(@Valid @RequestBody Productdtos productdtos){
        Productdtos created = prodcutService.create(productdtos);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    @PutMapping("/{productId}")
    public ResponseEntity<Productdtos> updateProduct(@Valid @PathVariable String productId, @RequestBody Productdtos productdtos){
        Productdtos updated = prodcutService.update( productdtos, productId);
        return new ResponseEntity<>(updated, HttpStatus.OK);

    }
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResposeClass> delete(@PathVariable String productId) throws IOException {
        prodcutService.delete(productId);
        ApiResposeClass build = ApiResposeClass.builder().message("Product id deleted").status(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(build, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public  ResponseEntity<Productdtos> getsingle(@PathVariable String productId){
        Productdtos productdtos = prodcutService.getsingle(productId);
        return new ResponseEntity<>(productdtos, HttpStatus.OK);
    }
    @GetMapping
    public  ResponseEntity<PegeableResponse<Productdtos>> getall( @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                  @RequestParam(value = "pageSize", defaultValue = "4", required = false)int pageSize,
                                                                  @RequestParam(value = "sortBy", defaultValue = "title", required = false)String sortBy,
                                                                  @RequestParam(value = "sortDir", defaultValue = "Asc", required = false)String sortDir){
    PegeableResponse<Productdtos> pegeableResponse = prodcutService.getAll(pageNumber, pageSize, sortBy, sortDir);
    return new ResponseEntity<>(pegeableResponse, HttpStatus.OK);
    }

    @GetMapping("/live")
    public  ResponseEntity<PegeableResponse<Productdtos>> getAllLive(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                     @RequestParam(value = "pageSize", defaultValue = "4", required = false)int pageSize,
                                                                     @RequestParam(value = "sortBy", defaultValue = "title", required = false)String sortBy,
                                                                     @RequestParam(value = "sortDir", defaultValue = "Asc", required = false)String sortDir) {
        PegeableResponse<Productdtos> pegeableResponse = prodcutService.findByLiveTrue(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pegeableResponse, HttpStatus.OK);
    }
    @GetMapping("/search/{query}")
    public  ResponseEntity<PegeableResponse<Productdtos>> SearchUSer(
            @PathVariable String query,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                     @RequestParam(value = "pageSize", defaultValue = "4", required = false)int pageSize,
                                                                     @RequestParam(value = "sortBy", defaultValue = "title", required = false)String sortBy,
                                                                     @RequestParam(value = "sortDir", defaultValue = "Asc", required = false)String sortDir) {
        PegeableResponse<Productdtos> pegeableResponse = prodcutService.searchByTitle(query, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pegeableResponse, HttpStatus.OK);
    }
    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadProductImage(@RequestParam("productImage")MultipartFile file, @PathVariable String productId) throws IOException {
        String imageName = fileService.uploadFile(file,imageUploadPath);
        Productdtos productdtos = prodcutService.getsingle(productId);
        productdtos.setProductImage(imageName);
        Productdtos productdtos1 = prodcutService.update(productdtos,productId);
        ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).success(true).httpStatus(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponse, HttpStatus.OK);
    }

    @GetMapping("/image/{productId}")
    public void servedProductImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
        Productdtos productdtos = prodcutService.getsingle(productId);
        logger.info(productdtos.getProductImage());
        InputStream inputStream = fileService.getResource(imageUploadPath, productdtos.getProductImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(inputStream, response.getOutputStream());
    }

}
