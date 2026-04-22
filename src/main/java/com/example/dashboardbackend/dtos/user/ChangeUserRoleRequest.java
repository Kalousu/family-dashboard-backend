/*
 * Copyright (c) 2026 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.example.dashboardbackend.dtos.user;

import com.example.dashboardbackend.models.enums.UserRole;

public record ChangeUserRoleRequest(
        UserRole userRole
) {

}
