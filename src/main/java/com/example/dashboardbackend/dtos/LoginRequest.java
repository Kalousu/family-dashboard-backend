package com.example.dashboardbackend.dtos;

public record LoginRequest(
        String name,
        String password
) {
}
