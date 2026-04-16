/*
 * Copyright (c) 2026 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.example.dashboardbackend.controllers;

import com.example.dashboardbackend.dtos.weather.GeoLocation;
import com.example.dashboardbackend.dtos.weather.WeatherRequest;
import com.example.dashboardbackend.dtos.weather.WeatherResponse;
import com.example.dashboardbackend.services.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/weather")

public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public WeatherResponse getWeather(@RequestParam double latitude, @RequestParam double longitude, @RequestParam String timezone) {
        WeatherRequest weatherRequest = new WeatherRequest(latitude, longitude, timezone);
        return weatherService.getWeather(weatherRequest);
    }

    @GetMapping("/search")
    public List<GeoLocation> searchCities(@RequestParam String city) {
        return weatherService.searchCities(city);
    }
}
