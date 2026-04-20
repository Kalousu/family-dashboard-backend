package com.example.dashboardbackend.dtos.picture;

public record PictureResponse(
    Long id,
    Long widgetId,
    String imageUrl
) {}