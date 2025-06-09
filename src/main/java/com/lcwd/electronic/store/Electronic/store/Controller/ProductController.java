package com.lcwd.electronic.store.Electronic.store.Controller;

import com.lcwd.electronic.store.Electronic.store.Service.ProdcutService;
import com.lcwd.electronic.store.Electronic.store.dtos.ApiResposeClass;
import com.lcwd.electronic.store.Electronic.store.dtos.PegeableResponse;
import com.lcwd.electronic.store.Electronic.store.dtos.Productdtos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProdcutService prodcutService;
    //create
    @PostMapping
    public ResponseEntity<Productdtos> createProdcut(@RequestBody Productdtos productdtos){
        Productdtos created = prodcutService.create(productdtos);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    @PutMapping("/{productId}")
    public ResponseEntity<Productdtos> updateProduct(@PathVariable String productId, @RequestBody Productdtos productdtos){
        Productdtos updated = prodcutService.update( productdtos, productId);
        return new ResponseEntity<>(updated, HttpStatus.OK);

    }
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResposeClass> delete(@PathVariable String productId){
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

}
