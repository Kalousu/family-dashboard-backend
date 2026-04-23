package com.example.dashboardbackend.dtos.user;

import com.example.dashboardbackend.models.enums.UserRole;
import jakarta.validation.constraints.NotNull;

public record ChangeUserRoleRequest(
        @NotNull(message = "User role is required")
        UserRole userRole
) {

}