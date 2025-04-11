package com.localconnct.api.service;

import com.localconnct.api.dto.*;
import com.localconnct.api.enums.Role;
import com.localconnct.api.exception.UserNotFoundException;
import com.localconnct.api.mapper.LocationMapper;
import com.localconnct.api.model.User;
import com.localconnct.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private  final UserDetailsServiceImpl userDetailsService;

    private final UserRepository userRepository;
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final LocationMapper locationMapper;
    private final JavaMailSender javaMailSender;
    private final OtpService otpService;


    public void saveNewUser(UserRequestDto request){
        try {
            User user = User.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .phone(request.getPhone())
                    .location(locationMapper.mapToLocation(request.getLocation()))
                    .roles(Arrays.asList(Role.USER.name()))
                    .build();

            userRepository.save(user);
        }catch (Exception e){
            log.error("Error :",e);
        }
    }

    public UserResponseDto findUserByEmail(String email) {
     User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User with email " + email + " not found");
        }
        return UserResponseDto.builder()
             .id(user.getId())
             .name(user.getName())
             .email(user.getEmail())
             .location(locationMapper.mapToLocationDto(user.getLocation()))
             .roles(user.getRoles())
             .build();
    }

    public String updateUserByEmail(String email,  UpdateUserDetailsDto request){
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User with email " + email + " not found");
        }
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setLocation(locationMapper.mapToLocation(request.getLocation()));
        userRepository.save(user);
        return "User Updated Successfully!";
    }

    public String changePassword(String email, ChangePasswordRequest request){
        User user = userRepository.findByEmail(email);
        if (passwordEncoder.matches(request.getCurrentPassword(),user.getPassword())){
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
            return "Password Changed.";

        }
        return "Incorrect Current Password!!!";
    }


    public void passwordInitiate(String email,String subject,String otp) {
      try {
         SimpleMailMessage mail = new SimpleMailMessage();
         mail.setTo(email);
         mail.setSubject(subject);
         mail.setText(otp);
         javaMailSender.send(mail);
      }catch (Exception e){
          log.error("Exception while sending otp :",e);
      }
    }

    public String  resetPassword(String email, ResetPasswordRequest request) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.warn("Password reset attempted for non-existing user: {}", email);
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        if (otpService.validateOtp(email,request.getOtp())){
            user.setPassword(passwordEncoder.encode(request.getConfirmPassword()));
            userRepository.save(user);
            return "Mr. "+user.getName()+", Your password has successfully updated!!!";
        }
        return "Password update failed: Invalid OTP!";
    }


    public void promoteToProvider(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        for (String role :  user.getRoles()){
            if (role.equals(Role.PROVIDER.name())) {
                throw new IllegalStateException("You're already a provider!");
            }
        }
        user.getRoles().add(Role.PROVIDER.name());
        userRepository.save(user);
        log.info("User {} promoted to PROVIDER", email);

    }


}
