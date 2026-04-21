package com.example.dashboardbackend.dtos.family;

public record CreateFamilyRequest(
        String familyName,
        String password,
        String email
) {
}
