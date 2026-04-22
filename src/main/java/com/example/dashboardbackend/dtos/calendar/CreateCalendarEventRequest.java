package com.example.dashboardbackend.dtos.calendar;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateCalendarEventRequest(
    String title,
    LocalDate date,
    String color,
    boolean allDay,
    LocalTime startTime
) {}