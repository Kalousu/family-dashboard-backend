

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