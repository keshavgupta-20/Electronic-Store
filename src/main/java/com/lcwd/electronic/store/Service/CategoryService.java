package com.lcwd.electronic.store.Service;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PegeableResponse;
import org.springframework.stereotype.Component;

@Component
public interface CategoryService {

    CategoryDto create(CategoryDto categorydto);
    CategoryDto update(CategoryDto categorydto, String categoryId);
    void delete(String categoyId);
    PegeableResponse <CategoryDto> getall(int pageNumber, int pageSize, String sortBy, String sortDir);
    CategoryDto getSingle(String categoryId);

}
