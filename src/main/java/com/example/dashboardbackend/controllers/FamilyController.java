package com.example.dashboardbackend.controllers;

import com.example.dashboardbackend.dtos.dashboard.DashboardResponse;
import com.example.dashboardbackend.services.FamilyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/family")
public class FamilyController {
    @Autowired
    FamilyService familyService;

    @GetMapping("/{familyId}/dashboard")
    public ResponseEntity<DashboardResponse> getDashboardForFamily(@PathVariable Long familyId) {
        return new ResponseEntity<>(familyService.getDashboardByFamilyId(familyId), HttpStatus.OK);
    }
}
