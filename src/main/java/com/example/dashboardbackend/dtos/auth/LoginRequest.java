/*
 * Copyright (c) 2026 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.example.dashboardbackend.dtos.auth;

public record LoginRequest(
        String name,
        String password
) {
}
