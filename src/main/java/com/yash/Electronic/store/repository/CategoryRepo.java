package com.yash.Electronic.store.repository;

import com.yash.Electronic.store.entites.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, String> {
}
