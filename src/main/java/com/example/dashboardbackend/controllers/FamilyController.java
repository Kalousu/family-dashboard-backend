package com.example.dashboardbackend.controllers;

import com.example.dashboardbackend.dtos.dashboard.DashboardResponse;
import com.example.dashboardbackend.dtos.family.CreateFamilyRequest;
import com.example.dashboardbackend.dtos.family.FamilyResponse;
import com.example.dashboardbackend.dtos.family.UpdateFamilyNameRequest;
import com.example.dashboardbackend.dtos.user.UserSelectPageResponse;
import com.example.dashboardbackend.services.FamilyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/family")
@RequiredArgsConstructor
public class FamilyController {
    private final FamilyService familyService;

    @GetMapping
    public ResponseEntity<List<FamilyResponse>> getFamilies() {
        return new ResponseEntity<>(familyService.getFamilies(), HttpStatus.OK);
    }

    @GetMapping("/{familyId}")
    public ResponseEntity<FamilyResponse> getFamilyById(
            @PathVariable Long familyId
    ) {
        return new ResponseEntity<>(familyService.getFamilyById(familyId), HttpStatus.OK);
    }

    @GetMapping("/{familyId}/dashboard")
    public ResponseEntity<DashboardResponse> getDashboardForFamily(@PathVariable Long familyId) {
        return new ResponseEntity<>(familyService.getDashboardByFamilyId(familyId), HttpStatus.OK);
    }

    @GetMapping("{familyId}/users")
    public ResponseEntity<UserSelectPageResponse> getUsersForFamily(@PathVariable Long familyId) {
        return new ResponseEntity<>(familyService.getUsersForFamily(familyId), HttpStatus.OK);
    }

    @PatchMapping("/{familyId}/name")
    public ResponseEntity<FamilyResponse> updateFamilyName(
            @PathVariable Long familyId,
            @RequestBody UpdateFamilyNameRequest updateFamilyNameRequest
    ) {
        familyService.updateFamilyName(familyId, updateFamilyNameRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> createFamily(
            @RequestBody CreateFamilyRequest createFamilyRequest
    ) {
        familyService.createFamily(createFamilyRequest);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{familyId}")
    public ResponseEntity<Object> deleteFamily(
            @PathVariable Long familyId
    ) {
        familyService.deleteFamilyById(familyId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
