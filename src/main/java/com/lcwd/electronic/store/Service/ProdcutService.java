package com.lcwd.electronic.store.Service;

import com.lcwd.electronic.store.dtos.PegeableResponse;
import com.lcwd.electronic.store.dtos.Productdtos;

import java.io.IOException;

public interface ProdcutService {
    Productdtos create(Productdtos productdtos);
    Productdtos update(Productdtos productdtos, String productId);
    void  delete(String productId) throws IOException;
    Productdtos getsingle(String productId);
    PegeableResponse<Productdtos> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);
    PegeableResponse<Productdtos> findByLiveTrue(int pageNumber, int pageSize, String sortBy, String sortDir);
    PegeableResponse<Productdtos> searchByTitle(String  subTitle, int pageNumber, int pageSize, String sortBy, String sortDir);
    //create product with category
    Productdtos createwithCategory(Productdtos productdtos, String categoryId);
    //update category of product
    Productdtos updateCategory(String productId, String categoryId);
    PegeableResponse<Productdtos> getAllOfCategory(String categoryId,int pageNumber, int pageSize, String sortBy, String sortDir);

}
