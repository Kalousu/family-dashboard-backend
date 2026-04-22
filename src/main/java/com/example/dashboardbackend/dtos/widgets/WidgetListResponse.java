

package com.example.dashboardbackend.dtos.widgets;

import com.example.dashboardbackend.models.widgets.WidgetConfig;
import com.example.dashboardbackend.models.widgets.WidgetPosition;

import java.time.LocalDateTime;

public record WidgetListResponse(
        Long id,
        String type,
        WidgetConfig widgetConfig,
        WidgetPosition position,
        LocalDateTime createdAt
) {
}
