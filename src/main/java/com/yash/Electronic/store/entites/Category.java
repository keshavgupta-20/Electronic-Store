package com.yash.Electronic.store.entites;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    @Column(name = "category_title", length = 20, nullable = false)
    private String title;
    @Column(name = "category_Desc")
    private String description;
    private String coverPage;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();
}
