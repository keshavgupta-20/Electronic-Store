package com.lcwd.electronic.store.Electronic.store.repositoreis;

import com.lcwd.electronic.store.Electronic.store.Entites.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo  extends JpaRepository<Product, String> {
    Page<Product> findBytitleContaining(String title, Pageable pageable);
    Page<Product> findByLiveTrue(Pageable pageable);
    //other method
    //custom finder method
}
