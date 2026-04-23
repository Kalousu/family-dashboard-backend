package com.example.dashboardbackend.dtos.timetable;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTimetableEventRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 200, message = "Title must not exceed 200 characters")
        String title,

        @Min(value = 0, message = "Slot must be >= 0")
        @Max(value = 23, message = "Slot must be <= 23")
        int slot,

        @Min(value = 0, message = "Day must be >= 0")
        @Max(value = 6, message = "Day must be <= 6")
        int day,

        @NotNull(message = "User ID is required")
        Long userId
) {}