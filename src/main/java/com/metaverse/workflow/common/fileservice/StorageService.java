package com.metaverse.workflow.common.fileservice;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface StorageService {
    void init();

    String store(MultipartFile file, Long programId, String folderName);

    String bulkExpenditureStore(MultipartFile file, Long programId, String folderName);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll(List<String> filePaths);
}
