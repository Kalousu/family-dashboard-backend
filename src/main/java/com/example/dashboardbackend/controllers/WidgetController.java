package com.example.dashboardbackend.controllers;

import com.example.dashboardbackend.dtos.UpdateWidgetConfigRequest;
import com.example.dashboardbackend.dtos.UpdateWidgetPositionRequest;
import com.example.dashboardbackend.dtos.widgets.CreateWidgetRequest;
import com.example.dashboardbackend.services.WidgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/widgets")
@RequiredArgsConstructor
public class WidgetController {
    private final WidgetService widgetService;

    @PostMapping
    public ResponseEntity<Object> createWidget(
            @RequestBody CreateWidgetRequest createWidgetRequest
    ) {
        widgetService.createWidget(createWidgetRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{widgetId}")
    public ResponseEntity<Object> deleteWidget(
            @PathVariable Long widgetId
    ){
        widgetService.deleteWidget(widgetId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{widgetId}/config")
    public ResponseEntity<Object> updateWidgetConfig(
            @PathVariable Long widgetId,
            @RequestBody UpdateWidgetConfigRequest updateWidgetConfigRequest
    ){
        widgetService.updateWidgetConfig(widgetId, updateWidgetConfigRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{widgetId}/position")
    public ResponseEntity<Object> updateWidgetPosition(
            @PathVariable Long widgetId,
            @RequestBody UpdateWidgetPositionRequest updateWidgetPositionRequest
    ){
        widgetService.updateWidgetPosition(widgetId, updateWidgetPositionRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
