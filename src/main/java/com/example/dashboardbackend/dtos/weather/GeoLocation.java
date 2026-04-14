/*
 * Copyright (c) 2026 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.example.dashboardbackend.dtos.weather;

public record GeoLocation(
        String name,
        double latitude,
        double longitude,
        String country,
        String timezone,
        String admin1 // Bundesland in open meteo
) {
}