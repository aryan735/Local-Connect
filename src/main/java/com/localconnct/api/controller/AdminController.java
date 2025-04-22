package com.localconnct.api.controller;

import com.localconnct.api.dto.BookingResponseDto;
import com.localconnct.api.dto.ServiceResponseDto;
import com.localconnct.api.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // Create new admin
    @PostMapping("/create/{userEmail}")
    public ResponseEntity<String> createUserToAdmin(@PathVariable("userEmail") String userEmail) {
        adminService.createAdmin(userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body("Admin created successfully");
    }

    @PostMapping("/delete-user/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email){
        adminService.deleteUser(email);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentAdminEmail = authentication.getName();
        log.info("Admin {} deleted user {}", currentAdminEmail, email);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User Deleted Successfully!");
    }
    @GetMapping("/get-all-services/{providerEmail}")
    public ResponseEntity<List<ServiceResponseDto>> getAllTheServices(@PathVariable String providerEmail){
        List<ServiceResponseDto> allServices = adminService.findAllServices(providerEmail);
        if (allServices==null){
          return new ResponseEntity<>(allServices,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allServices,HttpStatus.OK);
    }

    @GetMapping("/get-bookings")
    public ResponseEntity<List<BookingResponseDto>> getAllTheServices(){
        List<BookingResponseDto> bookings = adminService.findAllBookings();
        if (bookings==null){
            return new ResponseEntity<>(bookings,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(bookings,HttpStatus.OK);
    }

    @PutMapping("/update-role/{email}/{role}")
    public ResponseEntity<String> updateUserRole(@PathVariable String email, @PathVariable String role) {
        adminService.updateUserRole(email, role);
        return ResponseEntity.ok("User role updated to " + role);
    }


}
