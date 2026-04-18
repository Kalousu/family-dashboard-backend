package com.example.dashboardbackend.dtos;

import com.example.dashboardbackend.models.widgets.WidgetPosition;

public record UpdateWidgetPositionRequest(
        WidgetPosition widgetPosition
) {
}
