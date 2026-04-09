package com.example.dashboardbackend.dtos;

import com.example.dashboardbackend.models.Family;
import com.example.dashboardbackend.models.enums.UserRole;
import com.example.dashboardbackend.models.enums.UserPfp;

public record UserResponse(
        Long id,
        String name,
        UserRole userRole,
        Long familyId,
        UserPfp userPfp,
        String pfpColour
) {
}
