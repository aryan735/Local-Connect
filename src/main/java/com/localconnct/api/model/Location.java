package com.localconnct.api.model;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @NonNull
    private String city;
    @NonNull
    private String area;
    @NonNull
    private String state;
    @NonNull
    private String pincode;
    private double latitude;
    private double longitude;


}
