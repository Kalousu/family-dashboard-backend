

package com.example.dashboardbackend.controllers;

import com.example.dashboardbackend.dtos.weather.GeoLocationResponse;
import com.example.dashboardbackend.dtos.weather.WeatherRequest;
import com.example.dashboardbackend.dtos.weather.WeatherResponse;
import com.example.dashboardbackend.services.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping
    public ResponseEntity<WeatherResponse> getWeather(
            @RequestParam double latitude,
            @RequestParam double longitude, @RequestParam String timezone
    ) {
        WeatherRequest weatherRequest = new WeatherRequest(latitude, longitude, timezone);
        return new ResponseEntity<>(weatherService.getWeather(weatherRequest), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<GeoLocationResponse>> searchCities(
            @RequestParam String city
    ) {
        return new ResponseEntity<>(weatherService.searchCities(city), HttpStatus.OK);
    }
}
