package com.localconnct.api.dto;
import com.localconnct.api.enums.ServiceCategory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequestDto {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NonNull
    private ServiceCategory category;

    @NotBlank(message = "Provider ID is required")
    private String providerId;
    @NotBlank(message = "Provider Name is required")
    private String providerName;
    @NotBlank(message = "Provider Email is required")
    private String providerEmail;
@NotNull
private double price;
    @Valid
    private LocationDto location;
}
