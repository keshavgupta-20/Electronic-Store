package com.org.Electronic.store.repository;

import com.org.Electronic.store.entites.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, String> {
}
