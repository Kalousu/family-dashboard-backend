package com.example.dashboardbackend.dtos.calendar;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateCalendarEventRequest(
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    String title,

    @NotNull(message = "Date is required")
    LocalDate date,

    String color,

    boolean allDay,

    LocalTime startTime
) {}