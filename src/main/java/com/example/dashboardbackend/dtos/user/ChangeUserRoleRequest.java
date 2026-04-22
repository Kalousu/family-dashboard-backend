

package com.example.dashboardbackend.dtos.user;

import com.example.dashboardbackend.models.enums.UserRole;

public record ChangeUserRoleRequest(
        UserRole userRole
) {

}
