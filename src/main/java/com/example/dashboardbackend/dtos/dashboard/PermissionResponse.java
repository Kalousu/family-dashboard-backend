package com.example.dashboardbackend.dtos.dashboard;

public record PermissionResponse(
        boolean canEditLayout,
        boolean canAddWidgets,
        boolean canDeleteWidgets,
        boolean canEditWidgetData
) {
}
