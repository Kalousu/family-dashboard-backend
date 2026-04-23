package com.example.dashboardbackend.dtos.widgets;

import com.example.dashboardbackend.models.widgets.WidgetPosition;
import jakarta.validation.constraints.NotNull;

public record UpdateWidgetPositionRequest(
        @NotNull(message = "Widget position is required")
        WidgetPosition widgetPosition
) {
}