package com.example.dashboardbackend.controllers;

import com.example.dashboardbackend.dtos.widgets.CreateWidgetRequest;
import com.example.dashboardbackend.services.WidgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
