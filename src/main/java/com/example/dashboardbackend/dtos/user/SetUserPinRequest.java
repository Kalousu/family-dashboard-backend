package com.example.dashboardbackend.dtos.user;

import jakarta.validation.constraints.Pattern;

public record SetUserPinRequest(
    @Pattern(regexp = "^[0-9]{4,6}$", message = "PIN must be 4 to 6 digits")
    String pin
) {}