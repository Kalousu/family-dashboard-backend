/*
 * Copyright (c) 2026 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.example.dashboardbackend.dtos.weather;

public record WeatherRequest(
    double latitude,
    double longitude,
    String timezone
)
{
}
