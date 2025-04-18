package com.localconnct.api.service;

import com.localconnct.api.enums.Role;
import com.localconnct.api.exception.TokenBodyNotFoundException;
import com.localconnct.api.exception.UnauthorizedAccessException;
import com.localconnct.api.model.User;
import com.localconnct.api.repository.UserRepository;
import com.localconnct.api.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleAuthService {

    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    public String handleCallback(String code) {
        String tokenEndPoint = "https://oauth2.googleapis.com/token";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", "https://developers.google.com/oauthplayground");
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenEndPoint, request, Map.class);

        Map tokenBody = tokenResponse.getBody();
        if (tokenBody == null || !tokenBody.containsKey("id_token")) {
            throw new TokenBodyNotFoundException("Token response is invalid");
        }

        String idToken = (String) tokenBody.get("id_token");
        String userInfoUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
        ResponseEntity<Map> userInfoResponse = restTemplate.getForEntity(userInfoUrl, Map.class);

        if (userInfoResponse.getStatusCode() == HttpStatus.OK) {
            Map userInfo = userInfoResponse.getBody();
            assert userInfo != null;
            String email = (String) userInfo.get("email");
            String name = (String) userInfo.get("name");

            try {
                userDetailsService.loadUserByUsername(email);
            } catch (Exception e) {
                User user = new User();
                user.setEmail(email);
                user.setName(name);
                user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                user.setRoles(List.of(Role.USER.name()));
                userRepository.save(user);
            }

            return jwtUtil.generateToken(email);
        }

        throw new UnauthorizedAccessException("Unauthorized: Failed to fetch user info from Google");
    }
}
