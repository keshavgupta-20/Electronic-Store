package com.lcwd.electronic.store.Electronic.store.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateOrder {
    private String billingName;
    private String billingPhone;
    private String billingAddress;
}

