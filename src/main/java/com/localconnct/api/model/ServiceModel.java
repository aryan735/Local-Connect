package com.localconnct.api.model;

import com.localconnct.api.enums.ServiceCategory;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "services")
public class ServiceModel {
    @Id
    private String serviceId;
    @NonNull
    private String title;
    @NonNull
    private String description;

    @NotNull(message = "Category is required")
    private ServiceCategory category;

    private double price;
    @NonNull
    private String providerId;
    @NonNull
    private String providerName;
    @NonNull
    private String providerEmail;

    private double avgRating;

    private int totalRatings;
    private Location location;
}
