package com.example.dashboardbackend.dtos.widgets;

import com.example.dashboardbackend.models.widgets.general.WidgetPosition;

public record UpdateWidgetPositionRequest(
        WidgetPosition widgetPosition
) {
}
