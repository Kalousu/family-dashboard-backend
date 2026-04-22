package com.example.dashboardbackend.dtos.timetable;

public record TimetableEventResponse(Long id, String title, int slot, int day, Long userId) {}