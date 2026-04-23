package com.example.dashboardbackend.dtos.widgets;

import com.example.dashboardbackend.models.widgets.WidgetConfig;
import jakarta.validation.constraints.NotNull;

public record UpdateWidgetConfigRequest(
        @NotNull(message = "Widget config is required")
        WidgetConfig widgetConfig
) {

}