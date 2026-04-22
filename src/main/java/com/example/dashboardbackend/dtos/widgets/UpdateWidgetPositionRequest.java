/*
 * Copyright (c) 2026 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.example.dashboardbackend.dtos.widgets;

import com.example.dashboardbackend.models.widgets.WidgetPosition;

public record UpdateWidgetPositionRequest(
        WidgetPosition widgetPosition
) {
}
