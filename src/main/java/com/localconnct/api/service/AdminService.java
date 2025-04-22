package com.localconnct.api.service;

import com.localconnct.api.dto.BookingResponseDto;
import com.localconnct.api.dto.ServiceResponseDto;
import com.localconnct.api.enums.Role;
import com.localconnct.api.exception.*;
import com.localconnct.api.mapper.LocationMapper;
import com.localconnct.api.model.BookingModel;
import com.localconnct.api.model.ServiceModel;
import com.localconnct.api.model.User;
import com.localconnct.api.repository.BookingRepository;
import com.localconnct.api.repository.ServiceRepository;
import com.localconnct.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final LocationMapper locationMapper;
    private final BookingRepository bookingRepository;

    public void createAdmin(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        if (user==null){
            throw new UserNotFoundException("User with this email not found!");
        }
        // If the user already has the "ADMIN" role, no need to do anything
        if (user.getRoles().contains("ADMIN")) {
            throw new IllegalArgumentException("User is already an admin");
        }
        // Assign the "ADMIN" role to the user
        user.getRoles().add("ADMIN");
        // Save the updated user with the "ADMIN" role
        userRepository.save(user);
    }

    public void deleteUser(String email) {
        if (email==null || email.isEmpty()){
            throw new UserNotFoundException("Email not found!");
        }
        userRepository.deleteByEmail(email);
    }

    public List<ServiceResponseDto> findAllServices(String providerEmail) {
        if (providerEmail==null || providerEmail.isEmpty()){
            throw new EmailNotFoundException("Email not found!");
        }

        User provider = userRepository.findByEmail(providerEmail);
        if (!provider.getRoles().contains(Role.PROVIDER.name())){
            throw new ProviderNotFoundException("Provider Not Found! It's an user ac.");
        }
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

    public List<BookingResponseDto> findAllBookings() {
        List<BookingModel> bookings = bookingRepository.findAll();
        if (bookings.isEmpty()){
            throw new BookingNotFoundException("Bookings Not Found!");
        }

        return bookings.stream()
                .map(bookingModel -> BookingResponseDto.builder()
                        .bookingId(bookingModel.getBookingId())
                        .serviceId(bookingModel.getServiceId())
                        .providerId(bookingModel.getProviderId())
                        .userId(bookingModel.getUserId())
                        .status(bookingModel.getStatus())
                        .bookingTime(bookingModel.getBookingTime())
                        .paymentStatus(bookingModel.getPaymentStatus())
                        .build())
                .toList();

    }

    public void updateUserRole(String email, String role) {
        User user = userRepository.findByEmail(email);
        if (user==null){
            throw new UserNotFoundException("User Not Found!");
        }
        if (user.getRoles().contains(role)){
            throw new RolesNotFoundException("This user has already this role.");
        }
    }
}
