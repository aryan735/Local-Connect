package com.localconnct.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BookingRequestDto {
    @NotBlank(message = "Service ID is required")
    private String serviceId;

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Provider ID is required")
    private String providerId;
}
