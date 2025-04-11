package com.localconnct.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Please provide a valid email address.") //Makes sure no two users have same email
    private String email;
    @NotBlank(message = "Password is required")
    private String password; //will be stored as encrypted(BCrypt)

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Phone no is required")
    private String phone;

}
