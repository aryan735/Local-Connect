package com.localconnct.api.mapper;

import com.localconnct.api.model.Rating;

import java.util.List;

public class RatingMapper {

    public static double calculateRatingAverage(List<Rating> ratings) {
        return ratings.stream()
                .mapToDouble(Rating::getRating)
                .average()
                .orElse(0.0);
    }




}
