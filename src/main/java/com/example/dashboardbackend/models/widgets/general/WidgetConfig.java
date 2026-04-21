package com.example.dashboardbackend.models.widgets.general;

import java.util.Map;

public record WidgetConfig(
        String title,
        String color,
        Map<String, Object> settings
) {
}
