/*
 * Copyright (c) 2026 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.example.dashboardbackend.dtos.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Current(
        @JsonProperty("temperature_2m") double temperature,
        @JsonProperty("wind_speed_10m") double windSpeed,
        @JsonProperty("weather_code") int weatherCode
) {
}
