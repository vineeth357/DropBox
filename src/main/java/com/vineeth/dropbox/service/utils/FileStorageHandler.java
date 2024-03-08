package com.vineeth.dropbox.service.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileStorageHandler {

    @Value("${file.storage.directory}")
    private String fileStorageDirectory;

    public byte[] getFileContentByFilePath(String filePath) throws IOException{
        filePath = fileStorageDirectory + filePath;
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new IOException("File not found in Storage");
        }
        byte[] fileContent = Files.readAllBytes(path);
        if (fileContent == null) {
            throw new IOException("Failed to read file content");
        }
        return fileContent;
    }

    public void uploadFile(MultipartFile file, String filePath) throws IOException{
        filePath = fileStorageDirectory + filePath;
        Path path = Paths.get(filePath);
        Files.write(path, file.getBytes());
        return;
    }

    public void deleteFile(String filePath) throws IOException {
        filePath = fileStorageDirectory + filePath;
        Path path = Paths.get(filePath);
        try {
            Files.delete(path);
            System.out.println("File deleted successfully: " + filePath);
        } catch (IOException e) {
            System.err.println("Failed to delete file from: " + filePath);
            throw e;
        }
    }
    
}
