/*
 * Copyright (c) 2026 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.example.dashboardbackend.dtos.user;

import java.util.List;

public record UserSelectPageResponse(
        List<UserProfileResponse> userProfileResponses
) {
}
