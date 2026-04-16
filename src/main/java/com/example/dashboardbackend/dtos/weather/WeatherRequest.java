

package com.example.dashboardbackend.dtos.weather;

public record WeatherRequest(
    double latitude,
    double longitude,
    String timezone
)
{
}
