package com.localconnct.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bookings")
public class Booking {
    @Id
    private String id;
    @NonNull
    private String serviceId;
    @NonNull
    private String userId;
    @NonNull
    private String providerId;

    private LocalDateTime bookingTime;

    @NonNull
    private String status; //PENDING, CONFIRMED, COMPLETED, CANCELLED
}
