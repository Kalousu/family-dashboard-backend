package com.example.dashboardbackend.dtos;

import java.util.List;

public record FamilyLoginResponse(
        String token,
        List<UserProfile> profiles,
        String type
) {
}
