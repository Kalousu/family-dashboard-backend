package com.example.dashboardbackend.dtos.todo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record TodoUpdatePositionRequest(
        @NotNull(message = "ID is required")
        Long id,

        @Min(value = 0, message = "Sort order must be >= 0")
        int sortOrder
) {}