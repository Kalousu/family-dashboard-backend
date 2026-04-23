package com.example.dashboardbackend.dtos.timetable;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateWatchedUsersRequest(
        @NotNull(message = "User IDs list is required")
        List<Long> userIds
) {}