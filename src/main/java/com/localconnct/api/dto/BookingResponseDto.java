package com.localconnct.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingResponseDto {
    private String id;
    private String serviceId;
    private String userId;
    private String providerId;
    private String status;
    private LocalDateTime bookingTime;
}
