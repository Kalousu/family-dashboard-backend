package com.example.dashboardbackend.dtos.todo;

public record TodoItemResponse(
        Long id,
        String text,
        boolean completed,
        int sortOrder
) {}
