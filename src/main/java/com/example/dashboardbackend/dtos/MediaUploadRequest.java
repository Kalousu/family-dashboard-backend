package com.example.dashboardbackend.dtos;

import org.springframework.web.multipart.MultipartFile;

public record MediaUploadRequest (
        MultipartFile multipartFile
) {}
