package com.lcwd.electronic.store.repositoreis;

import com.lcwd.electronic.store.Entites.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, String> {
}
