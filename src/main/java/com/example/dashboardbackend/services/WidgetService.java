package com.example.dashboardbackend.services;

import com.example.dashboardbackend.dtos.widgets.UpdateWidgetConfigRequest;
import com.example.dashboardbackend.dtos.widgets.UpdateWidgetPositionRequest;
import com.example.dashboardbackend.dtos.widgets.WidgetListResponse;
import com.example.dashboardbackend.dtos.weather.WeatherRequest;
import com.example.dashboardbackend.dtos.widgets.CreateWidgetRequest;
import com.example.dashboardbackend.exceptions.DashboardNotFoundException;
import com.example.dashboardbackend.exceptions.WidgetNotFoundException;
import com.example.dashboardbackend.models.Dashboard;
import com.example.dashboardbackend.models.widgets.general.WidgetConfig;
import com.example.dashboardbackend.models.widgets.general.WidgetItem;
import com.example.dashboardbackend.repositories.DashboardRepository;
import com.example.dashboardbackend.repositories.WidgetItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WidgetService {
    private final WeatherService weatherService;
    private final DashboardRepository dashboardRepository;
    private final WidgetItemRepository widgetItemRepository;
    private final PictureService pictureService;

    public Object getWidgetData(Long widgetId, String type, WidgetConfig config) {
        return switch (type) {
            case "weather" -> getWeatherData(config);
            case "picture" -> getPictureData(widgetId);
            default -> null;
        };
    }

    private Object getWeatherData(WidgetConfig config) {
        return weatherService.getWeather(buildWeatherRequest(config.settings()));
    }

    private Object getPictureData(Long widgetId) {
        try {
            return pictureService.getPicture(widgetId);
        } catch (RuntimeException e) {
            return null;
        }
    }


    private WeatherRequest buildWeatherRequest(Map<String, Object> settings) {
        return new WeatherRequest(
                ((Number) settings.get("latitude")).doubleValue(),
                ((Number) settings.get("longitude")).doubleValue(),
                (String) settings.get("timezone")
        );
    }

    public void createWidget(CreateWidgetRequest createWidgetRequest) {
        Dashboard dashboard = dashboardRepository.findById(createWidgetRequest.dashboardId())
                .orElseThrow(() -> new DashboardNotFoundException("Dashboard with Id " + createWidgetRequest.dashboardId() + " doesn't exist"));

        WidgetItem widget = new WidgetItem();
        widget.setType(createWidgetRequest.type());
        widget.setDashboard(dashboard);
        widget.setWidgetConfig(createWidgetRequest.widgetConfig());
        widget.setWidgetPosition(createWidgetRequest.widgetPosition());
        widget.setCreatedAt(LocalDateTime.now());

        widgetItemRepository.save(widget);
    }

    public void deleteWidget(Long widgetId) {
        WidgetItem widgetToDelete = widgetItemRepository.findById(widgetId)
                .orElseThrow(() -> new WidgetNotFoundException("Widget you tried to delete not found"));

        widgetItemRepository.delete(widgetToDelete);
    }

    public void updateWidgetConfig(Long widgetId, UpdateWidgetConfigRequest updateWidgetConfigRequest) {
        WidgetItem widgetToUpdate = widgetItemRepository.findById(widgetId)
                .orElseThrow(() -> new WidgetNotFoundException("Widget you tried to update not found"));

        widgetToUpdate.setWidgetConfig(updateWidgetConfigRequest.widgetConfig());
        widgetItemRepository.save(widgetToUpdate);
    }

    public void updateWidgetPosition(Long widgetId, UpdateWidgetPositionRequest updateWidgetPositionRequest) {
        WidgetItem widgetToUpdate = widgetItemRepository.findById(widgetId)
                .orElseThrow(() -> new WidgetNotFoundException("Widget you tried to update not found"));

        widgetToUpdate.setWidgetPosition(updateWidgetPositionRequest.widgetPosition());
        widgetItemRepository.save(widgetToUpdate);
    }

    public List<WidgetListResponse> getWidgets() {
        return widgetItemRepository.findAll().stream().map(widgetItem ->
                new WidgetListResponse(
                        widgetItem.getId(),
                        widgetItem.getType(),
                        widgetItem.getWidgetConfig(),
                        widgetItem.getWidgetPosition(),
                        widgetItem.getCreatedAt()
                )
        ).toList();
    }

    public List<WidgetListResponse> getWidgetsByFamilyId(Long familyId) {
        Dashboard dashboard = dashboardRepository.findByFamily_Id(familyId)
                .orElseThrow(() -> new DashboardNotFoundException("Dashboard for Family " + familyId + " not found"));

        List<WidgetItem> widgetItems = dashboard.getWidgetItems();

        return widgetItems.stream().map(widgetItem ->
                new WidgetListResponse(
                        widgetItem.getId(),
                        widgetItem.getType(),
                        widgetItem.getWidgetConfig(),
                        widgetItem.getWidgetPosition(),
                        widgetItem.getCreatedAt()
                )
        ).toList();
    }
}
