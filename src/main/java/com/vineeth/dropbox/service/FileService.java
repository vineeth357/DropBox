package com.vineeth.dropbox.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.vineeth.dropbox.dto.FileMetadataDTO;
import com.vineeth.dropbox.entity.FileMetadata;

public interface FileService {
    Optional<FileMetadata> getFileMetadataById(Long fileId);
    byte[] getFileContentByFilePath(String filePath) throws IOException;
    List<FileMetadataDTO> getAllFileMetadata();
    String uploadFile(MultipartFile file, String fileName, String metadata) throws IOException;
    String updateFile(Long fileId, MultipartFile file, String metadata) throws IOException;
    String deleteFile(Long fileId) throws IOException;
}
