package com.example.dashboardbackend.dtos;

import java.util.List;

public record UserSelectPageResponse(
        List<UserProfile> userProfiles
) {
}
