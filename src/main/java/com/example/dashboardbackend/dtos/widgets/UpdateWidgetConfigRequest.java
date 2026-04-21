package com.example.dashboardbackend.dtos.widgets;

import com.example.dashboardbackend.models.widgets.general.WidgetConfig;

public record UpdateWidgetConfigRequest(
        WidgetConfig widgetConfig
) {

}
