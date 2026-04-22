package com.example.dashboardbackend.dtos.dashboard;

import com.example.dashboardbackend.dtos.user.UserResponse;
import java.util.List;

public record DashboardResponse(
        Long id,
        Long familyId,
        List<WidgetResponse> widgets,
        PermissionResponse permissions,
        UserResponse currentUser
) {
}
