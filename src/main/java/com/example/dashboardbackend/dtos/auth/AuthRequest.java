package com.example.dashboardbackend.dtos.auth;

public record AuthRequest(
        String name,
        String password
) {
}
