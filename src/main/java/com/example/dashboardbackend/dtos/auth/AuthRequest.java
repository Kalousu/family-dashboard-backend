package com.example.dashboardbackend.dtos.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Password is required")
        String password
) {
}