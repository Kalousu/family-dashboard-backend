package com.example.dashboardbackend.controllers;

import com.example.dashboardbackend.dtos.widgets.UpdateWidgetConfigRequest;
import com.example.dashboardbackend.dtos.widgets.UpdateWidgetPositionRequest;
import com.example.dashboardbackend.dtos.widgets.WidgetListResponse;
import com.example.dashboardbackend.dtos.widgets.CreateWidgetRequest;
import com.example.dashboardbackend.services.WidgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/widgets")
@RequiredArgsConstructor
public class WidgetController {
    private final WidgetService widgetService;

    @GetMapping
    public ResponseEntity<List<WidgetListResponse>> getWidgets(){
        return new ResponseEntity<>(widgetService.getWidgets(), HttpStatus.OK);
    }

    @GetMapping("/{familyId}")
    public ResponseEntity<List<WidgetListResponse>> getWidgetsByFamilyId(
            @PathVariable Long familyId
    ){
        return new ResponseEntity<>(widgetService.getWidgetsByFamilyId(familyId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> createWidget(
            @Valid @RequestBody CreateWidgetRequest createWidgetRequest
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
            @Valid @RequestBody UpdateWidgetConfigRequest updateWidgetConfigRequest
    ){
        widgetService.updateWidgetConfig(widgetId, updateWidgetConfigRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{widgetId}/position")
    public ResponseEntity<Object> updateWidgetPosition(
            @PathVariable Long widgetId,
            @Valid @RequestBody UpdateWidgetPositionRequest updateWidgetPositionRequest
    ){
        widgetService.updateWidgetPosition(widgetId, updateWidgetPositionRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}