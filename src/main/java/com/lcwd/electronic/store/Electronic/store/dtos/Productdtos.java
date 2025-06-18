package com.lcwd.electronic.store.Electronic.store.dtos;

import com.lcwd.electronic.store.Electronic.store.Entites.Category;
import com.lcwd.electronic.store.Electronic.store.Validate.ImageValidator;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Productdtos {
    private String productId;

    @NotBlank
    @Column(length = 30)
    private String title;
    @Column(length = 1000)
    private String description;
    @NotBlank
    private int price;

    @NotBlank
    private int quantity;
    @NotBlank
    private Date addedDate;
    private int discountedPrice;

    @NotBlank
    private boolean live;
    private boolean stock;
    @ImageValidator
    private String productImage;
    private CategoryDto category;

}
