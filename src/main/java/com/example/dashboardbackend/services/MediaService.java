package com.example.dashboardbackend.services;

import com.example.dashboardbackend.exceptions.FileUploadException;
import com.example.dashboardbackend.exceptions.UnsupportedMediaTypeException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaService {
    private final S3Client s3Client;

    @Value("${cloudflare.r2.bucket}")
    private String bucketName;

    @Value("${cloudflare.r2.public-url}")
    private String publicUrl;

    public String uploadFile(MultipartFile file) {

        String fileName = Optional.ofNullable(file.getOriginalFilename())
                .orElseThrow(() -> new UnsupportedMediaTypeException("Filename is missing"))
                .toLowerCase();

        String contentType = Optional.ofNullable(file.getContentType())
                .orElseThrow(() -> new UnsupportedMediaTypeException("Content type is missing"));

        String ext = getFileExtension(fileName);
        String folder = switch (ext) {
            case "jpg", "jpeg", "png", "gif" -> "images";
            default -> throw new UnsupportedMediaTypeException("Extension of type " + contentType + " not supported");
        };

        String key = String.format("%s/%s-%s", folder, UUID.randomUUID(), fileName);

        PutObjectRequest req = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType)
                .build();

        try {
            s3Client.putObject(req, RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            throw new FileUploadException("File upload to R2 failed, ", e);
        }

        return key;
    }

    private String getFileExtension(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index < 0 || index == fileName.length() - 1) {
            throw new IllegalArgumentException("Invalid File Extension in filename: " + fileName);
        }
        return fileName.substring(index + 1);
    }
}
