package com.localconnct.api.controller;


import com.localconnct.api.dto.*;
import com.localconnct.api.exception.UserNotFoundException;
import com.localconnct.api.service.OtpService;
import com.localconnct.api.service.RatingService;
import com.localconnct.api.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private  final AuthenticationManager authenticationManager;
 private final UserService userService;
 private final OtpService otpService;
 private final RatingService ratingService;

    @GetMapping
    public ResponseEntity<UserResponseDto> getUserDetails(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserResponseDto user = userService.findUserByEmail(email);
        if (user !=null){
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
      throw new UserNotFoundException("User Not Found!");

    }
    @PutMapping
    public ResponseEntity<String> updateUserById(@Valid  @RequestBody UpdateUserDetailsDto request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        String message = userService.updateUserByEmail(email, request);
        if (message != null && !message.isEmpty()){
            return new ResponseEntity<>(message,HttpStatus.OK);
        }
        return new ResponseEntity<>("User Updation Failed!!",HttpStatus.OK);
    }


    @PutMapping("/change-password")
    public ResponseEntity<String> changeCurrentPassword(@Valid @RequestBody ChangePasswordRequest request, HttpServletRequest httpServletRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email  = authentication.getName();
        String result = userService.changePassword(email, request);
        if (result.equals("Password Changed.")){
            try {
                httpServletRequest.logout();
                SecurityContextHolder.clearContext();
            } catch (ServletException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Password updated, but logout failed.");
            }
        }
        return ResponseEntity.ok("Password updated. Please log in again.");
    }


     @PostMapping("forgot-password-initiate")
    public ResponseEntity<String> forgotPasswordInitiate(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        userService.passwordInitiate(email,"Please don't share this OTP with anyone : ",otpService.generateOtp(email));
        return new ResponseEntity<>("Please Check Your Email, OTP is sent on your mail!!!",HttpStatus.CREATED) ;

    }

    @PutMapping("reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        String message = userService.resetPassword(email, request);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }


    @PutMapping("/promote-me-to-provider")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> promoteMeToProvider(Authentication authentication) {
        String email = authentication.getName();
        userService.promoteToProvider(email); // Promote the logged-in user
        return ResponseEntity.ok("Congrats! You are now a provider.");
    }



    @GetMapping("/providers")
    public ResponseEntity<List<UserResponseDto>> getAllProviders() {
        List<UserResponseDto> providers = userService.getAllProviders();
        return ResponseEntity.ok(providers);
    }
    @GetMapping("/provider-profile/providerId/{providerId}")
    public ResponseEntity<UserResponseDto> getProviderProfile(@PathVariable String providerId){
        if (providerId == null || providerId.isEmpty()){
            throw new UserNotFoundException("Please Enter providerId!");
        }

        UserResponseDto providerProfile = userService.getProviderProfile(providerId);
        if (providerProfile==null){
            throw new UserNotFoundException("Provider not found!");
        }
        return ResponseEntity.ok(providerProfile);
    }



}
