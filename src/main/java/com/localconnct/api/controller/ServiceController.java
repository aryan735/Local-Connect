package com.localconnct.api.controller;


import com.localconnct.api.dto.ServiceRequestDto;
import com.localconnct.api.dto.ServiceResponseDto;
import com.localconnct.api.enums.ServiceCategory;
import com.localconnct.api.exception.CategoryNotFoundException;
import com.localconnct.api.exception.UserNotFoundException;
import com.localconnct.api.model.User;
import com.localconnct.api.repository.UserRepository;
import com.localconnct.api.service.ServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/services")
public class ServiceController {

    private final ServiceService serviceService;
    private final UserRepository userRepository;


    @PreAuthorize("hasRole('PROVIDER')")
    @PostMapping("create-service")
    public ResponseEntity<String> createNewService(@Valid @RequestBody ServiceRequestDto requestDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        String service = serviceService.createService(email, requestDto);
        if (service.equals("Your service is registered Successfully!!!")){
            return new ResponseEntity<>(service, HttpStatus.OK);
        }
        return new ResponseEntity<>("Failed to register service!", HttpStatus.OK);
    }

    @GetMapping("get-services")
    public ResponseEntity<List<ServiceResponseDto>> AllServices(){

        List<ServiceResponseDto> allServices = serviceService.getAllServices();
        if (allServices != null){
            return new ResponseEntity<>(allServices,HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NOT_FOUND);
    }

    @PutMapping("update-services")
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> getAllServices(@Valid @RequestBody ServiceResponseDto requestDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        boolean isAvl = serviceService.updateService(email, requestDto);
        if (isAvl){
            return new ResponseEntity<>("Your Service is Updated Successfully!",HttpStatus.OK);
        }
        return new ResponseEntity<>("No matching service found for the given category!",HttpStatus.NO_CONTENT);

    }


    @GetMapping("/category/{serviceCategory}")
    public ResponseEntity<List<ServiceResponseDto>> getServiceByCategory(@PathVariable String serviceCategory){
        if (serviceCategory == null || serviceCategory.trim().isEmpty()){
            throw new CategoryNotFoundException("Category Not Found...Please provide the category!");
        }

        List<ServiceResponseDto> servicesByCategory = serviceService.getServicesByCategory(serviceCategory);
        if (servicesByCategory != null){
            return new ResponseEntity<>(servicesByCategory,HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NOT_FOUND);
    }
}
