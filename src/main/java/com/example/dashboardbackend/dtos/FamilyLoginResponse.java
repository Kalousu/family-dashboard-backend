package com.example.dashboardbackend.dtos;

import java.util.List;

public record FamilyLoginResponse(
        List<UserProfile> profiles,
        String type
) {
}
