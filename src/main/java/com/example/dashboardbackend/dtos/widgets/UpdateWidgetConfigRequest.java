

package com.example.dashboardbackend.dtos.widgets;

import com.example.dashboardbackend.models.widgets.WidgetConfig;

public record UpdateWidgetConfigRequest(
        WidgetConfig widgetConfig
) {

}
