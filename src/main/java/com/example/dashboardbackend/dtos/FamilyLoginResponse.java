package com.example.dashboardbackend.dtos;

import java.util.List;

public record FamilyLoginResponse(
        Long familyId,
        List<UserProfile> profiles,
        String role
) {
}
