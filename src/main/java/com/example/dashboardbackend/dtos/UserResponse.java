package com.example.dashboardbackend.dtos;

import com.example.dashboardbackend.models.Family;
import com.example.dashboardbackend.models.enums.UserRole;

public record UserResponse(
        Long id,
        String name,
        UserRole userRole,
        Family family
) {
}
