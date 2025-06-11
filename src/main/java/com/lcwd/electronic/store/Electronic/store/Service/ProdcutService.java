package com.lcwd.electronic.store.Electronic.store.Service;

import com.lcwd.electronic.store.Electronic.store.dtos.PegeableResponse;
import com.lcwd.electronic.store.Electronic.store.dtos.Productdtos;

import java.io.IOException;

public interface ProdcutService {
    Productdtos create(Productdtos productdtos);
    Productdtos update(Productdtos productdtos, String productId);
    void  delete(String productId) throws IOException;
    Productdtos getsingle(String productId);
    PegeableResponse<Productdtos> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);
    PegeableResponse<Productdtos> findByLiveTrue(int pageNumber, int pageSize, String sortBy, String sortDir);
    PegeableResponse<Productdtos> searchByTitle(String  subTitle, int pageNumber, int pageSize, String sortBy, String sortDir);

}
