package com.metaverse.workflow.common.fileservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    private Path rootLocation;

    StorageProperties properties;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.properties = properties;
        if(properties.getLocation().trim().length() == 0){
            throw new StorageException("File upload location can not be Empty.");
        }
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public String store(MultipartFile file, Long programId, String folderName) {
        String path;
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            Files.createDirectories(Paths.get(properties.getLocation() + programId + "/"+ folderName + "/"));
            this.rootLocation = Paths.get(properties.getLocation() + programId + "/"+ folderName + "/");
            Path destinationFile = this.rootLocation.resolve(
                            Paths.get(file.getOriginalFilename()))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
                path = destinationFile.toAbsolutePath().toString();
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
        return path;
    }

    @Override
    public String bulkExpenditureStore(MultipartFile file, Long agencyId, String folderName) {
        String path;
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            Files.createDirectories(Paths.get(properties.getLocation() + agencyId + "/"+ folderName + "/"));
            this.rootLocation = Paths.get(properties.getLocation() + agencyId + "/"+ folderName + "/");
            Path destinationFile = this.rootLocation.resolve(
                            Paths.get(file.getOriginalFilename()))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
                path = destinationFile.toAbsolutePath().toString();
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
        return path;
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll(List<String> filePaths) {
        for (String path : filePaths) {
            try {
                Path fileToDelete = rootLocation.resolve(path).normalize();
                FileSystemUtils.deleteRecursively(fileToDelete);
            } catch (IOException e) {
                System.err.println("Failed to delete file: " + path);
            }
        }
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

}
