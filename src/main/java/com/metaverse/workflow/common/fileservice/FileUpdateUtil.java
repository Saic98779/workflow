package com.metaverse.workflow.common.fileservice;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

public class FileUpdateUtil {

    private FileUpdateUtil() {
        // Utility class — prevent instantiation
    }

    /**
     * Safely replaces an old file with a new uploaded one.
     * - Saves the new file first
     * - Updates the given path in DB (via callback)
     * - Deletes the old file only after successful save
     *
     * @param file                the uploaded file (can be null)
     * @param existingFilePath    the old file path to delete
     * @param newFilePathSupplier callback to generate new storage path
     * @param afterSaveCallback   callback to run after new file is saved (like updating DB)
     * @return new file path (String) or old path if file is null
     */
    public static String replaceFile(
            MultipartFile file,
            String existingFilePath,
            FilePathSupplier newFilePathSupplier,
            Runnable afterSaveCallback
    ) {

        if (file == null || file.isEmpty()) {
            return existingFilePath;
        }

        try {
            // 1️⃣ Generate and save new file
            String newFilePath = newFilePathSupplier.generatePath(file);

            // 2️⃣ Execute callback (e.g., DB update)
            if (afterSaveCallback != null) {
                afterSaveCallback.run();
            }

            // 3️⃣ Delete old file safely
            safeDelete(existingFilePath);

            return newFilePath;
        } catch (Exception e) {
            throw new RuntimeException("File replacement failed: " + e.getMessage(), e);
        }
    }

    /**
     * Safely deletes a file (handles file-locks, missing files, etc.).
     */
    public static void safeDelete(String filePath) {
        if (filePath == null || filePath.isBlank()) return;

        Path path = Path.of(filePath);
        if (!Files.exists(path)) return;

        try {
            Files.delete(path);
        } catch (Exception e) {
            System.err.println("File delete failed, retrying: " + e.getMessage());
            try {
                Thread.sleep(300); // wait for OS lock release
                Files.deleteIfExists(path);
            } catch (Exception ex) {
                System.err.println("Unable to delete file even after retry: " + ex.getMessage());
            }
        }
    }

    /**
     * Functional interface for generating a new file path.
     */
    @FunctionalInterface
    public interface FilePathSupplier {
        String generatePath(MultipartFile file) throws IOException;
    }
}

