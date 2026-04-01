package com.metaverse.workflow.security.config;


import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class GlobalFileValidator {

    private static final List<String> ALLOWED_TYPES = List.of(
            "application/pdf",
            "image/png",
            "image/jpeg"
    );


    public void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) return;

        validateFile(file);
    }


    public void validate(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) return;

        for (MultipartFile file : files) {
            validateFile(file);
        }
    }

    private void validateFile(MultipartFile file) {

        String contentType = file.getContentType();

        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            throw new IllegalArgumentException(
                    "Invalid file type: " + file.getOriginalFilename() +
                            ". Only PDF, PNG, JPG are allowed."
            );
        }
    }
}