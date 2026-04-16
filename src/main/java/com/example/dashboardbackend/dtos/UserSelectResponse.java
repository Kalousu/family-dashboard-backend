package com.example.dashboardbackend.dtos;

import com.example.dashboardbackend.models.enums.UserRole;

public record UserSelectResponse(
        String token,
        Long userId,
        UserRole role
) {
}
