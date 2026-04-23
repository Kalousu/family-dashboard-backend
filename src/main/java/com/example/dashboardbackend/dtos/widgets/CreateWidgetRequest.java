package com.example.dashboardbackend.dtos.widgets;

import com.example.dashboardbackend.models.widgets.WidgetConfig;
import com.example.dashboardbackend.models.widgets.WidgetPosition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateWidgetRequest(
        @NotNull(message = "Dashboard ID is required")
        Long dashboardId,

        @NotBlank(message = "Widget type is required")
        String type,

        WidgetConfig widgetConfig,

        WidgetPosition widgetPosition
) {
}