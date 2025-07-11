package com.yash.Electronic.store.dtos;

import lombok.*;
import org.springframework.http.HttpStatus;

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
