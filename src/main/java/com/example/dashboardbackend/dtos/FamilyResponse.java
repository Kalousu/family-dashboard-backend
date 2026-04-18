package com.example.dashboardbackend.dtos;

public record FamilyResponse(
        Long id,
        String familyName,
        String email
) {
}
