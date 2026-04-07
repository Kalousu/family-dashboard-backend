package com.example.dashboardbackend.services;

import com.example.dashboardbackend.dtos.auth.AuthRequest;
import com.example.dashboardbackend.dtos.auth.AuthResponse;
import com.example.dashboardbackend.dtos.auth.RegisterRequest;
import com.example.dashboardbackend.models.enums.UserRole;
import com.example.dashboardbackend.repositories.UserRepository;
import com.example.dashboardbackend.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.dashboardbackend.models.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request){
        var user = new User();
        user.setName(request.name());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setUserRole(UserRole.CROOK);
        userRepository.save(user);
        String jwtToken = jwtUtils.generateToken(user);
        return new AuthResponse(jwtToken);
    }

    public AuthResponse authenticate(AuthRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.name(), request.password()));
        User user = userRepository.findByName(request.name()).orElseThrow(()-> new UsernameNotFoundException("Username not found"));
        String jwtToken = jwtUtils.generateToken(user);
        return new AuthResponse(jwtToken);
    }
}
