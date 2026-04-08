package com.example.dashboardbackend.dtos.dashboard;

public record Permissions(
        boolean canEditLayout,
        boolean canAddWidgets,
        boolean canDeleteWidgets,
        boolean canEditWidgetData
) {
}
