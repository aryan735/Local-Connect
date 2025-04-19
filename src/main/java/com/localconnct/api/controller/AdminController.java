package com.localconnct.api.controller;

import com.localconnct.api.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // Create new admin
    @PostMapping("/create/{userEmail}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createUserToAdmin(@PathVariable("userEmail") String userEmail) {
        adminService.createAdmin(userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body("Admin created successfully");
    }
   

}
