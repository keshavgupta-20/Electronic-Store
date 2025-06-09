package com.lcwd.electronic.store.Electronic.store.dtos;
import com.lcwd.electronic.store.Electronic.store.Validate.ImageValidator;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResposeClass {
    private String message;
    private HttpStatus status;
    private Boolean success;



}
