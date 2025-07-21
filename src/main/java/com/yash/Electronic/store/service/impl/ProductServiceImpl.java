package com.yash.Electronic.store.service.impl;

import com.yash.Electronic.store.entites.Category;
import com.yash.Electronic.store.entites.Product;
import com.yash.Electronic.store.exception.ResourceNotFoundException;
import com.yash.Electronic.store.helpers.Helper;
import com.yash.Electronic.store.service.ProdcutService;
import com.yash.Electronic.store.dtos.PageableResponse;
import com.yash.Electronic.store.dtos.ProductDto;
import com.yash.Electronic.store.repository.CategoryRepo;
import com.yash.Electronic.store.repository.ProductRepo;
import org.modelmapper.ModelMapper;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
    public ProductDto create(ProductDto productDto) {
        Product product = modelMapper.map(productDto, Product.class);
        String productId = UUID.randomUUID().toString();
        product.setProductId(productId);
        product.setAddedDate(new Date());
        Product saveproduct = productRepo.save(product);
        return modelMapper.map(saveproduct, ProductDto.class);
    }

    @Override
    public ProductDto update(ProductDto productDto, String productId) {
        Product product= productRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("id does not exist"));
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setQuantity(productDto.getQuantity());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());
        product.setProductImage(productDto.getProductImage());
        Product saveproduct = productRepo.save(product);
        return modelMapper.map(saveproduct, ProductDto.class);
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
    public ProductDto getsingle(String productId) {
        Product product = productRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Id does not found"));
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepo.findAll(pageable);
        return Helper.getPageableResponse(page, ProductDto.class);
    }

    @Override
    public List<ProductDto> findByLiveTrue() {
        Pageable pageable = PageRequest.of(0, 30);
        List<Product> products = productRepo.findByLiveTrue(pageable).getContent();


        return products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
    }



    @Override
    public PageableResponse<ProductDto> searchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepo.searchByTitle(subTitle, pageable);
        return Helper.getPageableResponse(page, ProductDto.class);
    }

    @Override
    public ProductDto createwithCategory(ProductDto productDto, String categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("CategoryID does not exist"));
        Product product = modelMapper.map(productDto, Product.class);
        String productId = UUID.randomUUID().toString();
        product.setProductId(productId);
        product.setAddedDate(new Date());
        product.setCategory(category);
        Product saveproduct = productRepo.save(product);
        return modelMapper.map(saveproduct, ProductDto.class);
    }

    @Override
    public ProductDto updateCategory(String productId, String categoryId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product id does not exist"));
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category does not exist"));
        product.setCategory(category);
        Product saveProduct =  productRepo.save(product);
        return modelMapper.map(saveProduct, ProductDto.class);

//        return null;
    }

    @Override
    public PageableResponse<ProductDto> getAllOfCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Category category = categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Id does not exist"));
        Page<Product> product = productRepo.findByCategory(category, pageable);
        return Helper.getPageableResponse(product, ProductDto.class);
    }

    public PageableResponse<ProductDto> getProductDiscounted(int pageNumber, int pageSize, String sortBy, String sortDir){
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> product = productRepo.findProductsWithDiscountMoreThan20Percent(pageable);
        return Helper.getPageableResponse(product, ProductDto.class);
    }


}
