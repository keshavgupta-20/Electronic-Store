package com.yash.Electronic.store.dtos;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseClass {
    private String message;
    private HttpStatus status;
    private Boolean success;


}
