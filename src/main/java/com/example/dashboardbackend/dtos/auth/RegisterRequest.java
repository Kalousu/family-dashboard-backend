package com.example.dashboardbackend.dtos.auth;

public record RegisterRequest(
        String name,
        String pin,
        String email,
        Long familyId,
        String pfpIcon
) {
}
