package com.yash.Electronic.store.dtos;

import com.yash.Electronic.store.validate.ImageValidator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CategoryDto {
    private String categoryId;

    @NotBlank
    @Size(min = 4, message = "title must be of size 4")
    private String title;
    @NotBlank(message = "Write some description")
    private String description;
    @ImageValidator
    private String coverPage;


}
