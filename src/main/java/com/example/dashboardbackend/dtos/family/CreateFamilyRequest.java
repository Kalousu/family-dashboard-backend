package com.example.dashboardbackend.dtos.family;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateFamilyRequest(
        @NotBlank(message = "Family name is required")
        @Size(min = 1, max = 100, message = "Family name must be between 1 and 100 characters")
        String familyName,

        @NotBlank(message = "Password is required")
        @Size(min = 4, max = 100, message = "Password must be between 4 and 100 characters")
        String password,

        String email
) {
}