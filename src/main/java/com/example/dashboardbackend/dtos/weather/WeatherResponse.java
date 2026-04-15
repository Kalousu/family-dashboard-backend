/*
 * Copyright (c) 2026 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.example.dashboardbackend.dtos.weather;
import com.fasterxml.jackson.annotation.JsonProperty;

public record WeatherResponse(
    @JsonProperty("current") Current current,
    @JsonProperty("daily") Daily daily
) {
}
