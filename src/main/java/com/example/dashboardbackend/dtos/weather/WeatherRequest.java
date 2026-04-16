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
  public static WeatherRequest fromSettings(Map<String, Object> settings) {
    return new WeatherRequest(
        ((Number) settings.get("latitude")).doubleValue(),
        ((Number) settings.get("longitude")).doubleValue(),
        (String) settings.get("timezone")
    );
  }
}
