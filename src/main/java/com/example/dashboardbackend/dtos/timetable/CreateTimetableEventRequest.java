package com.example.dashboardbackend.dtos.timetable;

public record CreateTimetableEventRequest(String title, int slot, int day, Long userId) {}