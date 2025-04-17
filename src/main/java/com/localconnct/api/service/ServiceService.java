package com.localconnct.api.service;


import com.localconnct.api.dto.ServiceRequestDto;
import com.localconnct.api.dto.ServiceResponseDto;

import com.localconnct.api.exception.ServiceNotFoundException;
import com.localconnct.api.exception.UserNotFoundException;
import com.localconnct.api.mapper.LocationMapper;
import com.localconnct.api.mapper.RatingMapper;
import com.localconnct.api.model.Rating;
import com.localconnct.api.model.ServiceModel;
import com.localconnct.api.model.User;
import com.localconnct.api.repository.RatingRepository;
import com.localconnct.api.repository.ServiceRepository;
import com.localconnct.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceService {


    private final ServiceRepository serviceRepository;
    private final LocationMapper locationMapper;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;

    public String  createService(String email,ServiceRequestDto requestDto) {
        User provider = userRepository.findByEmail(email);
        try {
            if (requestDto != null && provider != null){
                ServiceModel serviceModel = ServiceModel.builder()
                        .title(requestDto.getTitle())
                        .description(requestDto.getDescription())
                        .category(requestDto.getCategory())
                        .price(requestDto.getPrice())
                        .providerId(provider.getId())
                        .providerName(provider.getName())
                        .providerEmail(provider.getEmail())
                        .location(locationMapper.mapToLocation(requestDto.getLocation()))
                        .build();

                serviceRepository.save(serviceModel);
                if (provider.getServiceIdsProvided() == null) {
                    provider.setServiceIdsProvided(new ArrayList<>());
                }
                provider.getServiceIdsProvided().add(serviceModel.getServiceId());
                userRepository.save(provider);


                return "Your service is registered Successfully!!!";
            }
        }catch (Exception e){
            log.error(  "Service Data not found!!!",e);
            throw new ServiceNotFoundException("Unable to create service: " + e.getMessage());
        }
        throw new ServiceNotFoundException("Service data not found!");
    }
    
    public boolean updateService(String email, ServiceResponseDto requestDto){
        User user = userRepository.findByEmail(email);
        if (user == null){
        throw new UserNotFoundException("User not found with email: " + email);
        }
        // Update service details
           return serviceRepository.findByProviderId(user.getId())
                   .stream()
                   .filter(serviceModel ->serviceModel.getServiceId().equals(requestDto.getServiceId()))
                   .findFirst()
                   .map(serviceModel -> {
                       serviceModel.setTitle(requestDto.getTitle());
                       serviceModel.setDescription(requestDto.getDescription());
                       serviceModel.setPrice(requestDto.getPrice());
                       serviceModel.setLocation(locationMapper.mapToLocation(requestDto.getLocation()));
                       serviceRepository.save(serviceModel);
                       return true;
                   })
                   .orElse(false);

    }

    public List<ServiceResponseDto> getAllServices() {
        return serviceRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(ServiceModel::getAvgRating).reversed()) // top-rated first
                .map(serviceModel -> ServiceResponseDto.builder()
                            .serviceId(serviceModel.getServiceId())
                            .title(serviceModel.getTitle())
                            .description(serviceModel.getDescription())
                            .category(serviceModel.getCategory())
                            .price(serviceModel.getPrice())
                            .providerId(serviceModel.getProviderId())
                            .providerName(serviceModel.getProviderName())
                            .avgRating(serviceModel.getAvgRating())
                            .totalRatings(serviceModel.getTotalRatings())
                            .location(locationMapper.mapToLocationDto(serviceModel.getLocation()))
                            .build()
                )
                .toList();
    }

    public List<ServiceResponseDto> getServicesByCategory(String  serviceCategory) {

        return serviceRepository.findAll()
                .stream()
                .filter(serviceModel -> serviceModel.getCategory().name().equalsIgnoreCase(serviceCategory))
                .sorted(Comparator.comparing(ServiceModel::getAvgRating).reversed()) // 👈 top-rated first
                .map(serviceModel -> ServiceResponseDto.builder()
                        .serviceId(serviceModel.getServiceId())
                        .title(serviceModel.getTitle())
                        .description(serviceModel.getDescription())
                        .category(serviceModel.getCategory())
                        .price(serviceModel.getPrice())
                        .providerId(serviceModel.getProviderId())
                        .providerName(serviceModel.getProviderName())
                        .avgRating(serviceModel.getAvgRating())
                                .totalRatings(serviceModel.getTotalRatings())
                        .location(locationMapper.mapToLocationDto(serviceModel.getLocation()))
                        .build())
                        .toList();
    }


}
