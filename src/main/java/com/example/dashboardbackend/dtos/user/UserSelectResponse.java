package com.example.dashboardbackend.dtos.user;

import com.example.dashboardbackend.models.enums.UserRole;

public record UserSelectResponse(
        String token,
        Long userId,
        UserRole role
) {
}
