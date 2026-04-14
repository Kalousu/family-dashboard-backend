/*
 * Copyright (c) 2026 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.example.dashboardbackend.dtos.weather;

import java.util.List;

public record GeoLocationResponse(List<GeoLocation> results) {
}
