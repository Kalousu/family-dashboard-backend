package com.example.dashboardbackend.exceptions;

public class FileUploadException extends RuntimeException {
    public FileUploadException(String message, Throwable e) {
        super(message, e);
    }
}
