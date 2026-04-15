/*
 * Copyright (c) 2026 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.example.dashboardbackend.dtos.weather;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record Daily(
    @JsonProperty("time") List<String> dates,
    @JsonProperty("temperature_2m_max") List<Double> maxTemperatures,
    @JsonProperty("windspeed_10m_max") List<Double> maxWindSpeeds,
    @JsonProperty("weathercode") List<Integer> weatherCodes
){
}
