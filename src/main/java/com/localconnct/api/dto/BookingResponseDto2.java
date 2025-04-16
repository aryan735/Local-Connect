package com.localconnct.api.dto;

import com.localconnct.api.enums.BookingStatus;
import lombok.Data;

@Data
public class BookingResponseDto2 {
    private String bookingId;
    private String serviceId;
    private String userId;
    private String providerId;
    private BookingStatus status;
}
