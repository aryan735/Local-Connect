package com.localconnct.api.model;

import com.localconnct.api.enums.BookingStatus;
import com.localconnct.api.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "bookings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingModel {
    @Id
    private String bookingId;
    private String userId;
    private String providerId;
    private String serviceId;

    private LocalDateTime bookingTime;
    private BookingStatus status;


    private PaymentStatus paymentStatus = PaymentStatus.UNPAID;  // Default value is UNPAID

}
