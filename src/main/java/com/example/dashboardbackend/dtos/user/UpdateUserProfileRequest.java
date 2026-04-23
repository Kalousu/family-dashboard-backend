package com.example.dashboardbackend.dtos.user;

import com.example.dashboardbackend.models.enums.UserAvatarType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserProfileRequest(
    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
    String name,

    String color,

    String avatar,

    UserAvatarType avatarType
) {}