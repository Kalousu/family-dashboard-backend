package com.example.dashboardbackend.dtos;

public record UserSelectRequest(
        Long userId,
        String pin
) {
}
