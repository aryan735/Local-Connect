package com.localconnct.api.filter;

import com.localconnct.api.service.UserService;
import com.localconnct.api.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
         String authorizationHeader = request.getHeader("Authorization");
         String email = null;
         String  jwt = null;
         if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
             jwt = authorizationHeader.substring(7);
             email = jwtUtil.extractEmail(jwt);
         }

         if (email!=null){
             UserDetails userDetails = userDetailsService.loadUserByUsername(email);
             if (jwtUtil.validateToken(jwt)){
                 UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
               auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                 SecurityContextHolder.getContext().setAuthentication(auth);
             }
         }
         filterChain.doFilter(request,response);
    }

}
