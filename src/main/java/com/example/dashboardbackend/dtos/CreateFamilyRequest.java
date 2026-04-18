package com.example.dashboardbackend.dtos;

public record CreateFamilyRequest(
        String familyName,
        String password,
        String email
) {
}
