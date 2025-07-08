package com.lcwd.electronic.store.Electronic.store.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterUser {
    public   String UserId;
    @Size(min = 3, max = 18, message = "Name is not in proper format")
    public String name;

    @Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$",message = "Invalid format")
    @NotBlank(message = "Can't leave blank")
    public   String email;
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long, and include an uppercase letter, a lowercase letter, a digit, and a special character"
    )
    @NotBlank
    public String password;
    @NotBlank(message = "Not in format")
    public String gender;

    @NotBlank
    public   String about;
}
