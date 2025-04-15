package com.localconnct.api.dto;


import com.localconnct.api.enums.BookingStatus;
import com.localconnct.api.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponseDto {
    private String bookingId;
    private String providerId;
    private String serviceId;
    private String userId;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentTime;
    private BookingStatus status;
}
