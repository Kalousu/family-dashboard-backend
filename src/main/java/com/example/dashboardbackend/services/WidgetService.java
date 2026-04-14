package com.example.dashboardbackend.services;

import com.example.dashboardbackend.dtos.dashboard.WidgetResponse;
import com.example.dashboardbackend.dtos.widgets.CreateWidgetRequest;
import com.example.dashboardbackend.models.Dashboard;
import com.example.dashboardbackend.models.widgets.WidgetConfig;
import com.example.dashboardbackend.models.widgets.WidgetItem;
import com.example.dashboardbackend.repositories.DashboardRepository;
import com.example.dashboardbackend.repositories.WidgetItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class WidgetService {
    @Autowired
    WeatherService weatherService;

    @Autowired
    DashboardRepository dashboardRepository;

    @Autowired
    WidgetItemRepository widgetItemRepository;

    public Object getWidgetData(Long widgetId, String type, WidgetConfig config) {
        return switch (type) {
            case "weather" -> getWeatherData(config);
            default -> null;
        };
    }

    private Object getWeatherData(WidgetConfig config) {
        Map<String, Object> settings = config.settings();

        double latitude = ((Number) settings.get("latitude")).doubleValue();
        double longitude = ((Number) settings.get("longitude")).doubleValue();
        String timezone = (String) settings.get("timezone");

        return weatherService.getWeather(latitude, longitude, timezone);
    }

    public void createWidget(CreateWidgetRequest createWidgetRequest) {
        Dashboard dashboard = dashboardRepository.findById(createWidgetRequest.dashboardId())
                .orElseThrow(() -> new RuntimeException("Dashboard not found"));

        WidgetItem widget = new WidgetItem();
        widget.setType(createWidgetRequest.type());
        widget.setDashboard(dashboard);
        widget.setWidgetConfig(createWidgetRequest.widgetConfig());
        widget.setWidgetPosition(createWidgetRequest.widgetPosition());
        widget.setCreatedAt(LocalDateTime.now());

        widgetItemRepository.save(widget);
    }
}
