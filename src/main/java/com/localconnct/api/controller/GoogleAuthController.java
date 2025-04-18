package com.localconnct.api.controller;

import com.localconnct.api.service.GoogleAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth/google")
@RequiredArgsConstructor
public class GoogleAuthController {

    private final GoogleAuthService googleAuthService;

    @GetMapping("/callback")
    public ResponseEntity<Map<String, String>> handleGoogleCallBack(@RequestParam String code) {
        try {
            String token = googleAuthService.handleCallback(code);
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (Exception e) {
            log.error("Google login error: ", e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Authentication failed.");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

}
