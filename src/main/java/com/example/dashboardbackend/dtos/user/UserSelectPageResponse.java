package com.example.dashboardbackend.dtos.user;

import java.util.List;

public record UserSelectPageResponse(
        List<UserProfileResponse> userProfileResponses
) {
}
