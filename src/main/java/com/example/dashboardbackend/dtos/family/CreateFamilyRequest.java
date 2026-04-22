/*
 * Copyright (c) 2026 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.example.dashboardbackend.dtos.family;

public record CreateFamilyRequest(
        String familyName,
        String password,
        String email
) {
}
