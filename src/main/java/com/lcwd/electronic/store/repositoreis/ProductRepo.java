package com.lcwd.electronic.store.repositoreis;

import com.lcwd.electronic.store.Entites.Category;
import com.lcwd.electronic.store.Entites.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo  extends JpaRepository<Product, String> {
    Page<Product> findBytitleContaining(String title, Pageable pageable);
     Page<Product> findByLiveTrue(Pageable pageable);
     Page<Product> findByCategory(Category category, Pageable pageable);
    //other method
    //custom finder method
}
