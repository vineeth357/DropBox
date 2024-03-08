package com.vineeth.dropbox.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.vineeth.dropbox.dto.FileMetadataDTO;
import com.vineeth.dropbox.entity.FileMetadata;
import com.vineeth.dropbox.repository.FileMetadataRepository;
import com.vineeth.dropbox.service.utils.FileStorageHandler;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    @Autowired
    private FileStorageHandler fileStorageHandler;

    @Override
    public Optional<FileMetadata> getFileMetadataById(Long fileId) {
        return fileMetadataRepository.findById(fileId);
    }

    @Override
    public byte[] getFileContentByFilePath(String filePath) throws IOException {
        return fileStorageHandler.getFileContentByFilePath(filePath);
    }

    @Override
    public List<FileMetadataDTO> getAllFileMetadata() {
        List<FileMetadata> fileMetadataList = fileMetadataRepository.findAll();
        return FileMetadataDTO.fromEntityList(fileMetadataList);
    }

    @Override
    public String uploadFile(MultipartFile file, String fileName, String metadata) throws IOException {

        String filePath = fileName;

        // Save file to local storage
        fileStorageHandler.uploadFile(file, filePath);
        
        // Save metadata to database
        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setFileName(fileName);
        fileMetadata.setSize(file.getSize());
        fileMetadata.setFileType(file.getContentType());
        fileMetadata.setFilePath(filePath);
        fileMetadataRepository.save(fileMetadata);

        return String.valueOf(fileMetadata.getId());
    }

    @Override
    public String updateFile(Long fileId, MultipartFile file, String fileName) throws IOException {
        
        Optional<FileMetadata> optionalFileMetadata = fileMetadataRepository.findById(fileId);
        if (!optionalFileMetadata.isPresent()) {
            System.err.println("File not found with given fileId: " + fileId);
            throw new IOException();
        }

        FileMetadata fileMetadata = optionalFileMetadata.get();

        // // Save updated file to the local storage & update file_metadata
        if (file != null) {
            String filePath = fileMetadata.getFilePath();
            fileStorageHandler.uploadFile(file, filePath);
            if(!fileName.equals(null)){
                fileMetadata.setFileName(fileName);
            }
            fileMetadata.setFileType(file.getContentType());
            fileMetadata.setSize(file.getSize());
            fileMetadataRepository.save(fileMetadata);
            return "File updated successfully";
        }

        return "No updates provided";
    }

    @Override
    public String deleteFile(Long fileId) throws IOException{
        Optional<FileMetadata> optionalFileMetadata = fileMetadataRepository.findById(fileId);
        if (!optionalFileMetadata.isPresent()) {
            System.err.println("File not found with given fileId: " + fileId);
            throw new IOException();
        }

        FileMetadata fileMetadata = optionalFileMetadata.get();
        try{
            fileStorageHandler.deleteFile(fileMetadata.getFilePath());
            fileMetadataRepository.deleteById(fileId);
        }
        catch(IOException e){
            throw e;
        }
        
        return "File deleted successfully";
    }
}

