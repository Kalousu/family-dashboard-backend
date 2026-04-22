package com.example.dashboardbackend.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;
import org.springframework.lang.NonNullFields;
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
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        System.out.println("DEBUG JWT Filter: Processing request to " + request.getRequestURI());
        
        String tokenFromCookie = jwtUtils.extractFromCookie(request, "auth_token");
        String authorizationHeader = request.getHeader("Authorization");
        
        System.out.println("DEBUG JWT Filter: Token from cookie: " + (tokenFromCookie != null ? "PRESENT" : "NULL"));
        System.out.println("DEBUG JWT Filter: Authorization header: " + authorizationHeader);
        
        final String jwtToken;
        final String username;

        if (tokenFromCookie != null) {
            jwtToken = tokenFromCookie;
            System.out.println("DEBUG JWT Filter: Using token from cookie");
        } else if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
            System.out.println("DEBUG JWT Filter: Using token from Authorization header");
        } else {
            System.out.println("DEBUG JWT Filter: No token found, skipping authentication");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            username = jwtUtils.extractUsername(jwtToken);
            System.out.println("DEBUG JWT Filter: Extracted username: " + username);
        } catch (Exception e) {
            System.out.println("DEBUG JWT Filter: Failed to extract username: " + e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("DEBUG JWT Filter: Loading user details for: " + username);
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                System.out.println("DEBUG JWT Filter: User details loaded successfully");

                if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
                    System.out.println("DEBUG JWT Filter: Token is valid, setting authentication");
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                    // Debug log to check what type of principal we have
                    System.out.println("DEBUG JWT Filter: Principal type: " + userDetails.getClass().getSimpleName());
                    System.out.println("DEBUG JWT Filter: Principal: " + userDetails);
                } else {
                    System.out.println("DEBUG JWT Filter: Token is invalid");
                }
            } catch (Exception e) {
                System.out.println("DEBUG JWT Filter: Failed to load user details: " + e.getMessage());
            }
        } else if (username == null) {
            System.out.println("DEBUG JWT Filter: Username is null");
        } else {
            System.out.println("DEBUG JWT Filter: Authentication already exists");
        }

        filterChain.doFilter(request, response);
    }
}
