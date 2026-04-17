package com.example.dashboardbackend.dtos;

import com.example.dashboardbackend.models.enums.UserRole;

public record ChangeUserRoleRequest(
        UserRole userRole
) {

}
