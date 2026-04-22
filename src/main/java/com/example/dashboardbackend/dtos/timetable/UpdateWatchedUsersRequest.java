package com.example.dashboardbackend.dtos.timetable;

import java.util.List;

public record UpdateWatchedUsersRequest(List<Long> userIds) {}