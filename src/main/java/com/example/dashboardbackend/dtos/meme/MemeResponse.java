package com.example.dashboardbackend.dtos.meme;

public record MemeResponse(
    String title,
    String description,
    String imageUrl,
    String permalink,
    String author,
    int imageWidth,
    int imageHeight
) {}