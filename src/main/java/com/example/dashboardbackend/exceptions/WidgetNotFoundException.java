package com.example.dashboardbackend.exceptions;

public class WidgetNotFoundException extends RuntimeException {
    public WidgetNotFoundException(String message) {
        super(message);
    }
}
