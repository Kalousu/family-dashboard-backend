package com.example.dashboardbackend.dtos.todo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TodoUpdateTextRequest(
        @NotBlank(message = "Text is required")
        @Size(max = 500, message = "Text must not exceed 500 characters")
        String text
) {}