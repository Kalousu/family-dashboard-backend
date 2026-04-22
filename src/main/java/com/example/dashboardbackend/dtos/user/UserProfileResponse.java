

package com.example.dashboardbackend.dtos.user;

import com.example.dashboardbackend.models.enums.UserAvatarType;
import com.example.dashboardbackend.models.enums.UserRole;

public record UserProfileResponse(
        Long id,
        String name,
        String avatar,
        UserAvatarType avatarType,
        UserRole role,
        boolean hasPin,
        String color
) {
}
