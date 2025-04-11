package com.localconnct.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @NotNull(message = "New Password required")
    private String newPassword;
    @NotNull(message = "Confirmed Password required")
    private String confirmPassword;
    @NotNull(message = "Otp must required")
    private String otp;
}
