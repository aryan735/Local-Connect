package com.localconnct.api.mapper;

import com.localconnct.api.dto.LocationDto;
import com.localconnct.api.model.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {
    public Location mapToLocation(LocationDto locationDto) {
        return Location.builder()
                .latitude(locationDto.getLatitude())
                .longitude(locationDto.getLongitude())
                .city(locationDto.getCity())
                .state(locationDto.getState())
                .area(locationDto.getArea())
                .pincode(locationDto.getPincode())
                .build();
    }


    public LocationDto mapToLocationDto(Location location) {
        return LocationDto.builder()
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .city(location.getCity())
                .state(location.getState())
                .area(location.getArea())
                .pincode(location.getPincode())
                .build();
    }
}
