/*
 * Copyright (c) 2026 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.example.dashboardbackend.services;
import com.example.dashboardbackend.dtos.weather.GeoLocation;
import com.example.dashboardbackend.dtos.weather.GeoLocationResponse;
import com.example.dashboardbackend.dtos.weather.WeatherResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class WeatherService {
  private final RestClient restClient = RestClient.create();

  public WeatherResponse getWeather(double latitude, double longitude, String timezone) {
    String url = "https://api.open-meteo.com/v1/forecast" +
        "?latitude=" + latitude +
        "&longitude=" + longitude +
        "&current=temperature_2m,wind_speed_10m,weather_code" +
        "&timezone=" + timezone;

    return restClient.get()
                     .uri(url)
                     .retrieve()
                     .body(WeatherResponse.class);
  }

  public List<GeoLocation> searchCities(String city) {
    GeoLocationResponse geo = restClient.get()
                                        .uri("https://geocoding-api.open-meteo.com/v1/search" +
                                                 "?name={city}&count=5&language=de&format=json", city)
                                        .retrieve()
                                        .body(GeoLocationResponse.class);

    if (geo == null || geo.results() == null) return List.of();
    return geo.results();
  }

}
