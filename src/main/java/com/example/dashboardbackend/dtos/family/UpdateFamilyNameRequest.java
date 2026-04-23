package com.example.dashboardbackend.dtos.family;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateFamilyNameRequest(
        @NotBlank(message = "Family name is required")
        @Size(min = 1, max = 100, message = "Family name must be between 1 and 100 characters")
        String familyName
) {
}