package com.yash.Electronic.store.service;

import com.yash.Electronic.store.dtos.PageableResponse;
import com.yash.Electronic.store.dtos.ProductDto;

import java.io.IOException;
import java.util.List;

public interface ProdcutService {
    ProductDto create(ProductDto productDto);
    ProductDto update(ProductDto productDto, String productId);
    void  delete(String productId) throws IOException;
    ProductDto getsingle(String productId);
    PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);
    List<ProductDto> findByLiveTrue();
    PageableResponse<ProductDto> searchByTitle(String  subTitle, int pageNumber, int pageSize, String sortBy, String sortDir);
    //create product with category
    ProductDto createwithCategory(ProductDto productDto, String categoryId);
    //update category of product
    ProductDto updateCategory(String productId, String categoryId);
    PageableResponse<ProductDto> getAllOfCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir);
    PageableResponse<ProductDto> getProductDiscounted(int pageNumber, int pageSize, String sortBy, String sortDir);

}
