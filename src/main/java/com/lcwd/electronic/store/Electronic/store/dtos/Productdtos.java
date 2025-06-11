package com.lcwd.electronic.store.Electronic.store.dtos;

import com.lcwd.electronic.store.Electronic.store.Validate.ImageValidator;
import jakarta.persistence.Column;
import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Productdtos {
    private String productId;
    private String title;
    @Column(length = 1000)
    private String description;
    private int price;
    private int quantity;
    private Date addedDate;
    private int discountedPrice;
    private boolean live;
    private boolean stock;
    @ImageValidator
    private String productImage;

}
