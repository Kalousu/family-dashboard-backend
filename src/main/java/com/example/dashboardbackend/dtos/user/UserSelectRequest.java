package com.example.dashboardbackend.dtos.user;

import jakarta.validation.constraints.NotNull;

public record UserSelectRequest(
        @NotNull(message = "User ID is required")
        Long userId,

        String pin
) {
}