package com.example.dashboardbackend.dtos.user;

import com.example.dashboardbackend.models.enums.UserAvatarType;

public record UpdateUserProfileRequest(
    String name,
    String color,
    String avatar,
    UserAvatarType avatarType
) {}
