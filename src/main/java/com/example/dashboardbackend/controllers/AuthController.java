package com.example.dashboardbackend.controllers;

import com.example.dashboardbackend.dtos.auth.LoginRequest;
import com.example.dashboardbackend.dtos.user.UserSelectRequest;
import com.example.dashboardbackend.dtos.user.UserSelectResponse;
import com.example.dashboardbackend.dtos.auth.AuthRequest;
import com.example.dashboardbackend.dtos.auth.AuthResponse;
import com.example.dashboardbackend.dtos.auth.RegisterRequest;
import com.example.dashboardbackend.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AuthResponse> register(
            @RequestPart("data") RegisterRequest request,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar
    ) {
        return new ResponseEntity<>(authenticationService.register(request, avatar), HttpStatus.CREATED);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(
            @RequestBody AuthRequest request
    ) {
        return new ResponseEntity<>(authenticationService.authenticate(request), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Object response = authenticationService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/user/login")
    public ResponseEntity<UserSelectResponse> selectUser(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UserSelectRequest request
    ) {
        String familyToken = authHeader.replace("Bearer ", "");
        UserSelectResponse response = authenticationService.selectUser(familyToken, request);
        return ResponseEntity.ok(response);
    }
}
