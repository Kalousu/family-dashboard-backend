package com.example.dashboardbackend.dtos.dashboard;

import com.example.dashboardbackend.models.widgets.general.WidgetConfig;
import com.example.dashboardbackend.models.widgets.general.WidgetPosition;

import java.time.LocalDateTime;

public record WidgetResponse(
        Long id,
        String type,
        WidgetConfig widgetConfig,
        WidgetPosition position,
        LocalDateTime createdAt,
        Object data
) {
}
