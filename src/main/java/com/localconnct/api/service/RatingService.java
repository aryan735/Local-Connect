package com.localconnct.api.service;


import com.localconnct.api.dto.RatingListDto;
import com.localconnct.api.dto.RatingRequestDto;
import com.localconnct.api.dto.RatingResponseDto;
import com.localconnct.api.exception.RatingNotFoundException;
import com.localconnct.api.exception.ServiceNotFoundException;
import com.localconnct.api.exception.UserNotFoundException;
import com.localconnct.api.mapper.RatingMapper;
import com.localconnct.api.model.Rating;
import com.localconnct.api.model.ServiceModel;
import com.localconnct.api.model.User;
import com.localconnct.api.repository.RatingRepository;
import com.localconnct.api.repository.ServiceRepository;
import com.localconnct.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RatingService {

    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final ServiceRepository serviceRepository;

    public RatingResponseDto postRating(String email, RatingRequestDto dto) {
        User user = userRepository.findByEmail(email);
        if (user == null){
            throw new UserNotFoundException("User Not Found!");
        }
        Rating rating = new Rating();
        rating.setServiceId(dto.getServiceId());
        rating.setUserId(user.getId()); // set internally
        rating.setUserName(user.getName());
        rating.setRating(dto.getRating());
        rating.setReview(dto.getReview());
        rating.setCreatedAt(LocalDateTime.now());

        Rating savedRating = ratingRepository.save(rating);

        List<Rating> ratings = ratingRepository.findByServiceId(dto.getServiceId()); // Fetches ALL ratings including the new one
        double avgRating = RatingMapper.calculateRatingAverage(ratings);
        int totalRating  = ratings.size();
        ServiceModel serviceModel = serviceRepository.findById(dto.getServiceId())
                    .orElseThrow(()-> new ServiceNotFoundException("Service not found with ID: " + dto.getServiceId()));

        if (serviceModel != null){
            serviceModel.setAvgRating(avgRating); //adding the avgRating while rating a service
            serviceModel.setTotalRatings(totalRating);
            serviceRepository.save(serviceModel);
        }

        return RatingResponseDto.builder()
                .ratingId(savedRating.getRatingId())
               .userId(savedRating.getUserId())
               .userName(savedRating.getUserName())
               .serviceId(savedRating.getServiceId())
               .rating(savedRating.getRating())
               .review(savedRating.getReview())
               .createdAt(savedRating.getCreatedAt())
               .build();
    }

    public RatingListDto getAllRatingsOfAService(String serviceId) {
        List<Rating> ratings = ratingRepository.findByServiceId(serviceId);
        if (ratings==null){
            throw new RatingNotFoundException("Rating not found by this serviceId!");
        }

        ratings.sort(Comparator.comparing(Rating::getCreatedAt).reversed());

        List<RatingResponseDto> ratingDto = ratings.stream()
                .map(rating -> RatingResponseDto.builder()
                        .ratingId(rating.getRatingId())
                        .serviceId(rating.getServiceId())
                        .userId(rating.getUserId())
                        .userName(rating.getUserName())
                        .rating(rating.getRating())
                        .review(rating.getReview())
                        .createdAt(rating.getCreatedAt())
                        .build())
                .toList();

        return RatingListDto.builder()
                .ratings(ratingDto)
                .build();
    }

    public double getProviderAverageRating(String providerId) {
        List<ServiceModel> services = serviceRepository.findByProviderId(providerId);

        List<String> serviceIds = services.stream()
                .map(ServiceModel::getServiceId)
                .toList();

        List<Rating> ratings = ratingRepository.findByServiceIdIn(serviceIds);

        if (ratings.isEmpty()) return 0.0;

        return ratings.stream()
                .mapToDouble(Rating::getRating)
                .average()
                .orElse(0.0);
    }

    public String deleteTheRating(String providerId,String ratingId){

        List<ServiceModel> services = serviceRepository.findByProviderId(providerId);
        if (services==null){
            throw new ServiceNotFoundException("Services Not Found!");
        }
        List<String> serviceIds  = services.stream()
                        .map(ServiceModel::getServiceId)
                                .toList();
        List<Rating> ratings = ratingRepository.findByServiceIdIn(serviceIds);
        Optional<Rating> first = ratings.stream()
                .filter(rate -> rate.getRatingId().equalsIgnoreCase(ratingId))
                .findFirst();


        if (first.isPresent()){
            ratingRepository.deleteById(ratingId);
            return "Rating is deleted where RatingId is : "+ratingId;
        }
        return "Rating Not found with this id!";
    }

}
