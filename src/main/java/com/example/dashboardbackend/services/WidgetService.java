package com.example.dashboardbackend.services;

import com.example.dashboardbackend.models.widgets.WidgetConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WidgetService {
    @Autowired
    WeatherService weatherService;

    public Object getWidgetData(Long widgetId, String type, WidgetConfig config){
        return switch(type) {
            case "weather" -> getWeatherData(config);
            default -> null;
        };
    }

    private Object getWeatherData(WidgetConfig config){
        Map<String, Object> settings = config.settings();

        double latitude = ((Number) settings.get("latitude")).doubleValue();
        double longitude = ((Number) settings.get("longitude")).doubleValue();
        String timezone = (String) settings.get("timezone");

        return weatherService.getWeather(latitude, longitude, timezone);
    }
}
