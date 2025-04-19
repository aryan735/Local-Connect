package com.localconnct.api.service;

import com.localconnct.api.exception.UserNotFoundException;
import com.localconnct.api.model.User;
import com.localconnct.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;

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
}
