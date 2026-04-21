package com.example.dashboardbackend.dtos.media;

import org.springframework.web.multipart.MultipartFile;

public record MediaUploadRequest(
        MultipartFile multipartFile
) {
}
