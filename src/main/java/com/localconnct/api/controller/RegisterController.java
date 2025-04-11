package com.localconnct.api.controller;

import com.localconnct.api.dto.LoginRequest;
import com.localconnct.api.dto.RegisterUserRequest;
import com.localconnct.api.dto.UserRequestDto;
import com.localconnct.api.service.UserDetailsServiceImpl;
import com.localconnct.api.service.UserService;
import com.localconnct.api.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/register")
public class RegisterController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final JwtUtil jwtUtil;


    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody UserRequestDto request){
          userService.saveNewUser(request);
        return    ResponseEntity.ok("User Registered Successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest user){
        try{
     authenticationManager.authenticate(
             new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword()));
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(user.getEmail());
         String  jwt = jwtUtil.generateToken(userDetails.getUsername());
         return new ResponseEntity<>(jwt, HttpStatus.OK);
    }catch (Exception e){
            log.error("Exception occurred while generating createAuthenticationToken",e);
            return new ResponseEntity<>("Incorrect Email or Password.",HttpStatus.BAD_REQUEST);
        }
    }




}
