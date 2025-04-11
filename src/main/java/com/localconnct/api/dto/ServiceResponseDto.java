package com.localconnct.api.dto;

import com.localconnct.api.enums.ServiceCategory;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceResponseDto {
    private String serviceId;
    private String title;
    private String description;
    private ServiceCategory category;
    private double price;
    private String providerId;
    private String providerName;
    private double avgRating;
    private int totalRatings;
    private LocationDto location;
}
