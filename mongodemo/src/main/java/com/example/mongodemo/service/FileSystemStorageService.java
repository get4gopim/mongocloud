package com.example.mongodemo.service;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import com.example.mongodemo.config.StorageProperties;
import com.example.mongodemo.endpoint.FileUploadController;
import com.example.mongodemo.exception.StorageException;
import com.example.mongodemo.exception.StorageFileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemStorageService.class);

    private final Path rootLocation;

    @Autowired
    private ContributionNumberService contributionNumberService;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
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
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
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

    @Override
    public String uploadFile(final MultipartFile file) {
        UUID uuid = UUID.randomUUID();
        String fileName = uuid.toString().replaceAll("-", "");

        if (file.getOriginalFilename().contains(".")) {
           String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
           fileName += fileExtension;
        }

        LOGGER.info("handleFileUpload- fileName:{}, orginalFileName:{} ", fileName, file.getOriginalFilename());

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                final Path filePath = this.rootLocation.resolve(fileName);
                LOGGER.info("handleFileUpload- filePath:{} ", filePath.toFile());
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(filePath.toFile()));
                        //new BufferedOutputStream(new FileOutputStream(new File(fileName)));
                stream.write(bytes);
                stream.close();
                LOGGER.info("You successfully uploaded {} !", fileName);
                return fileName;
            } catch (Exception e) {
                LOGGER.info("You failed to upload {} => {} ", fileName, e.getMessage());
                return null;
            }
        } else {
            LOGGER.info("You failed to upload {} because the file was empty.", fileName);
            return null;
        }

    }

    @Async
    public CompletableFuture<String> storeContributionIds(final String fileName) {
        final Path filePath = this.rootLocation.resolve(fileName);

        LOGGER.info("storeContributionIds- filePath:{} ", filePath.toFile().getPath());

        contributionNumberService.readAndSaveAll(filePath.toFile().getPath());

        deleteFile(filePath);

        return CompletableFuture.completedFuture("DONE");
    }

    private void deleteFile(Path filePath) {
        if (filePath.toFile().exists()) {
            FileSystemUtils.deleteRecursively(filePath.toFile());
        }

        if (!filePath.toFile().exists()) {
            LOGGER.info("File Deleted Successfully - filePath:{} ", filePath.toFile().getPath());
        } else {
            LOGGER.warn("File Not Deleted - filePath:{} ", filePath.toFile().getPath());
        }
    }
}
