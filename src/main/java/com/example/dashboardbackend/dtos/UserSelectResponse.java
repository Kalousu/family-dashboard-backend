package com.example.dashboardbackend.dtos;

import com.example.dashboardbackend.models.enums.UserRole;

public record UserSelectResponse(
        Long userId,
        UserRole role
) {
}
