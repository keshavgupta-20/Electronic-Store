package com.org.Electronic.store.service;

import com.org.Electronic.store.dtos.CategoryDto;
import com.org.Electronic.store.dtos.PageableResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CategoryService {

    CategoryDto create(CategoryDto categorydto);
    CategoryDto update(CategoryDto categorydto, String categoryId);
    void delete(String categoyId);
    PageableResponse<CategoryDto> getall(int pageNumber, int pageSize, String sortBy, String sortDir);
    CategoryDto getSingle(String categoryId);
    List<CategoryDto> getAllCategory();

}
