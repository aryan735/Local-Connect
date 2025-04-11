package com.localconnct.api.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "ratings")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rating {

    @Id
    private String ratingId;
    @NonNull
    private String userId;       // The user who gave the rating
    private String userName;     // (Optional) for display purposes
    private String serviceId;    // Which service is being rated

    private double rating;          // 1 to 5
    private String review;       // Optional review message

    private LocalDateTime createdAt;
}
