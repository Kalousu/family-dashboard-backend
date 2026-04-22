/*
 * Copyright (c) 2026 SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.example.dashboardbackend.dtos.media;

import org.springframework.web.multipart.MultipartFile;

public record MediaUploadRequest(
        MultipartFile multipartFile
) {
}
