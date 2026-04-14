package com.example.dashboardbackend.dtos.widgets;

import com.example.dashboardbackend.models.widgets.WidgetConfig;
import com.example.dashboardbackend.models.widgets.WidgetPosition;

public record CreateWidgetRequest(
        Long dashboardId,
        String type,
        WidgetConfig widgetConfig,
        WidgetPosition widgetPosition
) {
}
