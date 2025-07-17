package com.yash.Electronic.store.dtos;

import com.yash.Electronic.store.entites.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ContactDetailDto {
    private String contactDetailId;

    @NotBlank
    private String houseNo;

    @NotBlank
    private String street;

    @NotBlank
    private String city;

    @Pattern(regexp = "\\d{6}", message = "Pincode must be 6 digits")
    private String pincode;

    @NotBlank
    private String country;

    @Pattern(regexp = "\\d{10}", message = "Contact number must be 10 digits")
    private String contactNo;

    private String userId;

}