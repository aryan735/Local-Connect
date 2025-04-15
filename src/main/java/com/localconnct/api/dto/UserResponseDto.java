package com.localconnct.api.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserResponseDto {
    private String id;
    private String name;

    private String phone;
    private double avgRating;
    private int totalRating;
    private List<String> roles;
    private LocationDto location;
}
