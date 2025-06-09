package com.lcwd.electronic.store.Electronic.store.Service.impl;

import com.lcwd.electronic.store.Electronic.store.Entites.Category;
import com.lcwd.electronic.store.Electronic.store.Exception.ResourceNotFoundException;
import com.lcwd.electronic.store.Electronic.store.Helpers.helper;
import com.lcwd.electronic.store.Electronic.store.Service.CategoryService;
import com.lcwd.electronic.store.Electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.Electronic.store.dtos.PegeableResponse;
import com.lcwd.electronic.store.Electronic.store.repositoreis.CategoryRepo;
import lombok.experimental.Helper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
@Service
public class CategoryServiceimpl implements CategoryService {


    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Value("${category.profile.image.path}")
    private String imagePath;
    Logger logger = LoggerFactory.getLogger(CategoryServiceimpl.class);
    @Override
    public CategoryDto create(CategoryDto categorydto) {
        String categoryId = UUID.randomUUID().toString();
        categorydto.setCategoryId(categoryId);
        Category category = modelMapper.map(categorydto, Category.class);
        Category savedCategory = categoryRepo.save(category);
        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public CategoryDto update(CategoryDto categorydto, String categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Id does not exist"));
        category.setTitle(categorydto.getTitle());
        category.setCoverPage(categorydto.getCoverPage());
        category.setTitle(categorydto.getTitle());
        Category updatedCategory = categoryRepo.save(category);
        return modelMapper.map(updatedCategory, CategoryDto.class);
    }

    @Override
    public void delete(String categoyId) {
        Category category  = categoryRepo.findById(categoyId).orElseThrow(() -> new ResourceNotFoundException("Id does not exist"));
        String fullpath = imagePath+category.getCoverPage();
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
        categoryRepo.delete(category);
    }

    @Override
    public PegeableResponse<CategoryDto> getall(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Category> page = categoryRepo.findAll(pageable);
        PegeableResponse<CategoryDto> pegeableResponse = helper.getPageableResponse(page, CategoryDto.class);
        return  pegeableResponse;
    }

    @Override
    public CategoryDto getSingle(String categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Id does not found"));
        return modelMapper.map(category, CategoryDto.class);
    }


}
