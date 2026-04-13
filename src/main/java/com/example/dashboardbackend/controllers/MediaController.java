package com.example.dashboardbackend.controllers;

import com.example.dashboardbackend.dtos.MediaUploadRequest;
import com.example.dashboardbackend.services.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {
    private final MediaService mediaService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(
            @RequestPart("file") MultipartFile file
    ){
        String key = mediaService.uploadFile(file);
        return ResponseEntity.ok(key);
    }
}
