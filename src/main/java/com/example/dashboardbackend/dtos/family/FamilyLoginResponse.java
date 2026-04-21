package com.example.dashboardbackend.dtos.family;

import com.example.dashboardbackend.dtos.user.UserProfileResponse;

import java.util.List;

public record FamilyLoginResponse(
        String token,
        List<UserProfileResponse> profiles,
        String type
) {
}
