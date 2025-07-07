package com.lcwd.electronic.store.Electronic.store.dtos;

import lombok.*;
import org.springframework.stereotype.Service;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {

    private String token;
    UserDto userDto;
    private String refreshToken;
}
