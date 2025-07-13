package com.yash.Electronic.store.service;

import com.yash.Electronic.store.dtos.CategoryDto;
import com.yash.Electronic.store.dtos.PageableResponse;
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
