package com.localconnct.api.dto;

import com.localconnct.api.enums.BookingStatus;
import com.localconnct.api.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingResponseDto {
    private String bookingId;
    private String serviceId;
    private String userId;
    private String providerId;
    private BookingStatus status;
    private LocalDateTime bookingTime;
    private PaymentStatus paymentStatus;
}
