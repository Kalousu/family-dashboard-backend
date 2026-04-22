package com.example.dashboardbackend.dtos.dashboard;

import java.util.List;

public record DashboardResponse(
        Long id,
        Long familyId,
        List<WidgetResponse> widgets,
        PermissionResponse permissionResponse
) {
}
