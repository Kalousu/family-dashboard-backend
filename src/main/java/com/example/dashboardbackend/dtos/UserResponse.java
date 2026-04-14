package com.example.dashboardbackend.dtos;

import com.example.dashboardbackend.models.enums.UserRole;
import com.example.dashboardbackend.models.enums.UserAvatarType;

public record UserResponse(
        Long id,
        String name,
        UserRole userRole,
        Long familyId,
        String avatar,
        UserAvatarType avatarType,
        String pfpColour
) {
}
