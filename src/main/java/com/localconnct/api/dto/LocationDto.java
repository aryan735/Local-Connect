package com.localconnct.api.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {

    @NotNull(message = "Latitude is required")
    private double latitude;

    @NotNull(message = "Longitude is required")
    private double longitude;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Area is required")
    private String area;



    @NotBlank(message = "Pincode is required")
    private String pincode;
}

