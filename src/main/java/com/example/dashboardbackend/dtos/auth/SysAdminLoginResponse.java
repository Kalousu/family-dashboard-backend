package com.example.dashboardbackend.dtos.auth;

public record SysAdminLoginResponse(
        String token,
        String type
) {
}
