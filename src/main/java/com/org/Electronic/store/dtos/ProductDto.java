package com.org.Electronic.store.dtos;

import com.org.Electronic.store.validate.ImageValidator;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private String productId;

    @NotBlank
    @Column(length = 30)
    private String title;
    @Column(length = 1000)
    private String description;

    private int price;


    private int quantity;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date addedDate;
    private int discountedPrice;


    private boolean live =false ;
    private boolean stock = false;
    @ImageValidator
    private String productImage;
    private CategoryDto category;


}
