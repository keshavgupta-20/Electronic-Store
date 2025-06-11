package com.lcwd.electronic.store.Electronic.store.Service.impl;

import com.lcwd.electronic.store.Electronic.store.Entites.Category;
import com.lcwd.electronic.store.Electronic.store.Entites.Product;
import com.lcwd.electronic.store.Electronic.store.Exception.ResourceNotFoundException;
import com.lcwd.electronic.store.Electronic.store.Helpers.helper;
import com.lcwd.electronic.store.Electronic.store.Service.ProdcutService;
import com.lcwd.electronic.store.Electronic.store.dtos.PegeableResponse;
import com.lcwd.electronic.store.Electronic.store.dtos.Productdtos;
import com.lcwd.electronic.store.Electronic.store.repositoreis.CategoryRepo;
import com.lcwd.electronic.store.Electronic.store.repositoreis.ProductRepo;
import lombok.Setter;
import lombok.experimental.Helper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProdcutService {

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryRepo categoryRepo;

    @Value("${product.profile.image.path}")
    private String imagePath;
    @Override
    public Productdtos create(Productdtos productdtos) {
        Product product = modelMapper.map(productdtos, Product.class);
        String productId = UUID.randomUUID().toString();
        product.setProductId(productId);
        product.setAddedDate(new Date());
        Product saveproduct = productRepo.save(product);
        return modelMapper.map(saveproduct, Productdtos.class);
    }

    @Override
    public Productdtos update(Productdtos productdtos, String productId) {
        Product product= productRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("id does not exist"));
        product.setTitle(productdtos.getTitle());
        product.setDescription(productdtos.getDescription());
        product.setPrice(productdtos.getPrice());
        product.setDiscountedPrice(productdtos.getDiscountedPrice());
        product.setQuantity(productdtos.getQuantity());
        product.setLive(productdtos.isLive());
        product.setStock(productdtos.isStock());
        product.setProductImage(productdtos.getProductImage());
        Product saveproduct = productRepo.save(product);
        return modelMapper.map(saveproduct, Productdtos.class);
    }

    @Override
    public void delete(String productId) throws IOException {

        Product product = productRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Id does not found"));
        String fullPath = imagePath+product.getProductImage();
        Path path = Paths.get(fullPath);
        Files.delete(path);
        productRepo.delete(product);
    }

    @Override
    public Productdtos getsingle(String productId) {
        Product product = productRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Id does not found"));
        return modelMapper.map(product, Productdtos.class);
    }

    @Override
    public PegeableResponse<Productdtos> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepo.findAll(pageable);
        return helper.getPageableResponse(page, Productdtos.class);
    }

    @Override
    public PegeableResponse<Productdtos> findByLiveTrue(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepo.
                findByLiveTrue(pageable);
        return helper.getPageableResponse(page, Productdtos.class);
    }

    @Override
    public PegeableResponse<Productdtos> searchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepo.findBytitleContaining(subTitle, pageable);
        return helper.getPageableResponse(page, Productdtos.class);
    }

    @Override
    public Productdtos createwithCategory(Productdtos productdtos, String categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("CategoryID does not exist"));
        Product product = modelMapper.map(productdtos, Product.class);
        String productId = UUID.randomUUID().toString();
        product.setProductId(productId);
        product.setAddedDate(new Date());
        product.setCategory(category);
        Product saveproduct = productRepo.save(product);
        return modelMapper.map(saveproduct, Productdtos.class);
    }

    @Override
    public Productdtos updateCategory(String productId, String categoryId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product id does not exist"));
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category does not exist"));
        product.setCategory(category);
        Product saveProduct =  productRepo.save(product);
        return modelMapper.map(saveProduct, Productdtos.class);

//        return null;
    }

    @Override
    public PegeableResponse<Productdtos> getAllOfCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Category category = categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Id does not exist"));
        Page<Product> product = productRepo.findByCategory(category, pageable);
        return helper.getPageableResponse(product, Productdtos.class);
    }


}
