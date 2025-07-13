package com.yash.Electronic.store.repository;

import com.yash.Electronic.store.entites.Category;
import com.yash.Electronic.store.entites.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo  extends JpaRepository<Product, String> {
    Page<Product> findBytitleContaining(String title, Pageable pageable);



    @Query("SELECT p FROM Product p WHERE p.live = true AND p.quantity > 0")
    Page<Product> findByLiveTrue(Pageable pageable);
     Page<Product> findByCategory(Category category, Pageable pageable);
    //other method
    //custom finder method
}
