package com.example.dashboardbackend.services;

import com.example.dashboardbackend.dtos.dashboard.DashboardResponse;
import com.example.dashboardbackend.dtos.dashboard.WidgetResponse;
import com.example.dashboardbackend.models.Dashboard;
import com.example.dashboardbackend.repositories.DashboardRepository;
import com.example.dashboardbackend.repositories.FamilyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FamilyService {
    @Autowired
    FamilyRepository familyRepository;
    @Autowired
    DashboardRepository dashboardRepository;
    @Autowired
    private WidgetService widgetService;

    public DashboardResponse getDashboardByFamilyId(Long familyId){
        Dashboard dashboard = dashboardRepository.findByFamily_Id(familyId).orElseThrow(()-> new RuntimeException("No dashboard found"));

        List<WidgetResponse> widgetResponseList = dashboard.getWidgetItems().stream().map(widgetItem -> {
            Object data = null;
            if (widgetItem.getWidgetConfig() != null && 
                widgetItem.getWidgetConfig().settings() != null && 
                !widgetItem.getWidgetConfig().settings().isEmpty()) {
                data = widgetService.getWidgetData(widgetItem.getId(), widgetItem.getType(), widgetItem.getWidgetConfig());
            }
            
            return new WidgetResponse(
                widgetItem.getId(),
                widgetItem.getType(),
                widgetItem.getWidgetConfig(),
                widgetItem.getWidgetPosition(),
                widgetItem.getCreatedAt(),
                data
            );
        }).toList();

        return new DashboardResponse(
                dashboard.getId(),
                dashboard.getFamily().getId(),
                widgetResponseList,
                null
        );
    }
}
