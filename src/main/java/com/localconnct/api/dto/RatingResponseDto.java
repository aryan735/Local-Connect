package com.localconnct.api.dto;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RatingResponseDto {

    private String ratingId;
    private String userId;
    private String userName;
    private String serviceId;
    private double rating;
    private String review;
    private LocalDateTime createdAt;
}
