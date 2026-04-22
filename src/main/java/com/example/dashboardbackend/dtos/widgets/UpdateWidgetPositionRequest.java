

package com.example.dashboardbackend.dtos.widgets;

import com.example.dashboardbackend.models.widgets.WidgetPosition;

public record UpdateWidgetPositionRequest(
        WidgetPosition widgetPosition
) {
}
