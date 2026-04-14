package com.example.dashboardbackend.dtos.auth;

public record RegisterRequest(
        String name,
        String password,
        String email,
        Long familyId,
        String pfpIcon
) {
}
