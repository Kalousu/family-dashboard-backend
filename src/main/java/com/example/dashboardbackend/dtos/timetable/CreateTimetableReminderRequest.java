package com.example.dashboardbackend.dtos.timetable;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTimetableReminderRequest(
        @Min(value = 0, message = "Day must be >= 0")
        @Max(value = 6, message = "Day must be <= 6")
        int day,

        @NotBlank(message = "Text is required")
        @Size(max = 500, message = "Text must not exceed 500 characters")
        String text
) {}