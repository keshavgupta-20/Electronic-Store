package com.yash.Electronic.store.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yash.Electronic.store.validate.ImageValidator;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Month;
import java.time.Year;
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
