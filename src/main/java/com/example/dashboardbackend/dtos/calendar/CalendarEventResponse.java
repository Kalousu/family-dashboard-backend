package com.example.dashboardbackend.dtos.calendar;

import java.time.LocalDate;
import java.time.LocalTime;

public record CalendarEventResponse(
    Long id,
    String title,
    LocalDate date,
    String color,
    boolean allDay,
    LocalTime startTime
) {}