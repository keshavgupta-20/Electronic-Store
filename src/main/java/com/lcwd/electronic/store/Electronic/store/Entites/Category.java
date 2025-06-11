package com.lcwd.electronic.store.Electronic.store.Entites;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @Column(name = "id")
    private String categoryId;

    @Column(name = "category_title", length = 60, nullable = false)
    private String title;
    @Column(name = "category_Desc", length = 50)
    private String description;
    private String coverPage;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();
}
