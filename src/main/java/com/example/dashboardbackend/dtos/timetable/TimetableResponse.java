package com.example.dashboardbackend.dtos.timetable;

import java.util.List;

public record TimetableResponse(
    List<TimetableEventResponse> events,
    List<TimetableReminderResponse> reminders,
    List<Long> watchedUserIds
) {}