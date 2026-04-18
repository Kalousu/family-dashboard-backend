package com.example.dashboardbackend.exceptions;

public class DashboardNotFoundException extends RuntimeException {
    public DashboardNotFoundException(String message) {
        super("Dashboard not found: " + message);
    }
}
