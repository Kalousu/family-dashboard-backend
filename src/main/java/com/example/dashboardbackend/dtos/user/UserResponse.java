

package com.example.dashboardbackend.dtos.user;

import com.example.dashboardbackend.models.enums.UserRole;
import com.example.dashboardbackend.models.enums.UserAvatarType;
import com.example.dashboardbackend.models.enums.UserColorMode;

public record UserResponse(
        Long id,
        String name,
        UserRole role,  // Changed from userRole to role
        Long familyId,
        String avatar,
        UserAvatarType avatarType,
        String pfpColour,
        UserColorMode userColorMode,
        boolean hasPin  // Added hasPin field
) {
}
