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

    public WidgetResponse addWidget(Long familyId, Map<String, Object> widgetData) {
        Dashboard dashboard = dashboardRepository.findByFamily_Id(familyId)
                .orElseThrow(() -> new RuntimeException("Dashboard not found for family"));

        WidgetItem widget = new WidgetItem();
        widget.setType((String) widgetData.get("type"));
        widget.setDashboard(dashboard);
        
        @SuppressWarnings("unchecked")
        Map<String, String> position = (Map<String, String>) widgetData.get("position");
        widget.setWidgetPosition(new com.example.dashboardbackend.models.widgets.WidgetPosition(
            position.get("col"),
            position.get("row"),
            position.get("colSpan"),
            position.get("rowSpan")
        ));
        
        widget.setWidgetConfig(new WidgetConfig("", "", Map.of()));
        widget.setCreatedAt(LocalDateTime.now());

        WidgetItem savedWidget = widgetItemRepository.save(widget);

        return new WidgetResponse(
            savedWidget.getId(),
            savedWidget.getType(),
            savedWidget.getWidgetConfig(),
            savedWidget.getWidgetPosition(),
            savedWidget.getCreatedAt(),
            null
        );
    }

    public void removeWidget(Long familyId, Long widgetId) {
        WidgetItem widget = widgetItemRepository.findById(widgetId)
                .orElseThrow(() -> new RuntimeException("Widget not found"));
        
        if (!widget.getDashboard().getFamily().getId().equals(familyId)) {
            throw new RuntimeException("Widget does not belong to this family");
        }

        widgetItemRepository.delete(widget);
    }
}
