package com.example.dashboardbackend.dtos.user;

public record UserSelectRequest(
        Long userId,
        String pin
) {
}
