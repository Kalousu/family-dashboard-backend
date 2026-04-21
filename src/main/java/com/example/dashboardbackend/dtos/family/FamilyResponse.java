package com.example.dashboardbackend.dtos.family;

public record FamilyResponse(
        Long id,
        String familyName,
        String email
) {
}
