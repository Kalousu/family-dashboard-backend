

package com.example.dashboardbackend.dtos.user;

import com.example.dashboardbackend.models.enums.UserRole;

public record UserSelectResponse(
        Long userId,
        UserRole role
) {
}
