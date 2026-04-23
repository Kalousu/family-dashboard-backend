package com.example.dashboardbackend.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Name is required")
        @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
        String name,

        String pin,

        String email,

        @NotNull(message = "Family ID is required")
        Long familyId,

        String pfpIcon,

        String color
) {
}