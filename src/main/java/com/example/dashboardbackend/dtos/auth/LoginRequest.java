package com.example.dashboardbackend.dtos.auth;

public record LoginRequest(
        String name,
        String password
) {
}
