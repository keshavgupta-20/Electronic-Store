package com.yash.Electronic.store.entites;

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
    @Column(name = "live", nullable = false)
    private Boolean live = false;

    @Column(name = "stock", nullable = false)
    private boolean stock =false;
    private  int discountedPrice;
    private String productImage;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private  Category category;
}
