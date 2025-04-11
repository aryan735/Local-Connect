package com.localconnct.api.dto;

import jakarta.validation.constraints.*;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingRequestDto {

    @NotBlank(message = "Service ID is required")
    private String serviceId;

    @NotBlank(message = "User name is required")
    private String userName;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating can be at most 5")
    private double rating;

    private String review;
}

