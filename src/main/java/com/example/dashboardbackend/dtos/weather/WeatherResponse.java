
package com.example.dashboardbackend.dtos.weather;
import com.fasterxml.jackson.annotation.JsonProperty;

public record WeatherResponse(
    @JsonProperty("current") CurrentResponse currentResponse,
    @JsonProperty("daily") DailyResponse dailyResponse
) {
}
