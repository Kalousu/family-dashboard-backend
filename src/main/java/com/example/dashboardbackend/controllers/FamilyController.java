package com.example.dashboardbackend.controllers;

import com.example.dashboardbackend.dtos.dashboard.DashboardResponse;
import com.example.dashboardbackend.dtos.dashboard.WidgetResponse;
import com.example.dashboardbackend.services.FamilyService;
import com.example.dashboardbackend.services.WidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/families")
public class FamilyController {
    @Autowired
    FamilyService familyService;
    
    @Autowired
    WidgetService widgetService;

    @GetMapping("/{familyId}/dashboard")
    public ResponseEntity<DashboardResponse> getDashboardForFamily(@PathVariable Long familyId){
        return new ResponseEntity<>(familyService.getDashboardByFamilyId(familyId), HttpStatus.OK);
    }
    
    @PostMapping("/{familyId}/dashboard/widgets")
    public ResponseEntity<WidgetResponse> addWidget(
            @PathVariable Long familyId,
            @RequestBody Map<String, Object> widgetData
    ){
        WidgetResponse widget = widgetService.addWidget(familyId, widgetData);
        return new ResponseEntity<>(widget, HttpStatus.CREATED);
    }
    
    @DeleteMapping("/{familyId}/dashboard/widgets/{widgetId}")
    public ResponseEntity<Void> removeWidget(
            @PathVariable Long familyId,
            @PathVariable Long widgetId
    ){
        widgetService.removeWidget(familyId, widgetId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
