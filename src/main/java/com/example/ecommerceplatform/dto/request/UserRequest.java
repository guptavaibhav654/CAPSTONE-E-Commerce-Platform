package com.example.ecommerceplatform.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequest {

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 30,
            message = "Username must be between 3 and 30 characters")
    private String userName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Enter a valid email")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 100,
            message = "Password must be between 8 and 100 characters")
    private String password;

}
