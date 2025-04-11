package com.localconnct.api.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotNull(message = "Current Password is required.")
    private String currentPassword;
    @NotNull(message = "Confirm Password is required.")
    private String newPassword;
}
