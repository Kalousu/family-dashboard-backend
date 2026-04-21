package com.example.dashboardbackend.dtos.widgets;

import com.example.dashboardbackend.models.widgets.general.WidgetConfig;
import com.example.dashboardbackend.models.widgets.general.WidgetPosition;

public record CreateWidgetRequest(
        Long dashboardId,
        String type,
        WidgetConfig widgetConfig,
        WidgetPosition widgetPosition
) {
}
