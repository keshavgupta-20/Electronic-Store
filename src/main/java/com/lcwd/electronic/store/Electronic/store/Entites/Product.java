package com.lcwd.electronic.store.Electronic.store.Entites;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    private String productId;
    private String title;
    @Column(length = 1000)
    private String description;
    private int price;
    private int quantity;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private  int discountedPrice;
    private String productImage;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private  Category category;
}
