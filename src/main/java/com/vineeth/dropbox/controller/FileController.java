package com.vineeth.dropbox.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.vineeth.dropbox.dto.FileMetadataDTO;
import com.vineeth.dropbox.entity.FileMetadata;
import com.vineeth.dropbox.service.FileService;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    // Retrieve all files metadata from the database
    @GetMapping
    public ResponseEntity<List<FileMetadataDTO>> listFiles() {
        List<FileMetadataDTO> fileMetadataList = fileService.getAllFileMetadata();
        return new ResponseEntity<>(fileMetadataList, HttpStatus.OK);
    }

    // Retrieve file from storage based on fileId provided
    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> readFile(@PathVariable Long fileId) {

        Optional<FileMetadata> optionalFileMetadata = fileService.getFileMetadataById(fileId);
        if (!optionalFileMetadata.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        FileMetadata fileMetadata = optionalFileMetadata.get();
        try {
            System.out.println("Reading File from path: " + fileMetadata.getFilePath());
            byte[] fileBytes = fileService.getFileContentByFilePath(fileMetadata.getFilePath());
            ByteArrayResource resource = new ByteArrayResource(fileBytes);
            return ResponseEntity.ok()
                    .contentLength(fileBytes.length)
                    .header("Content-type", "application/octet-stream")
                    .header("Content-disposition", "attachment; filename=\"" + fileMetadata.getFileName() + "\"")
                    .body(resource);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to read file: " + e.getMessage());
        }
    }

    // upload new file to the storage
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("fileContent") MultipartFile file,
                                             @RequestParam("fileName") String fileName,
                                             @RequestParam(value = "metadata", required = false) String metadata) {
        try {
            String fileId = fileService.uploadFile(file, fileName, metadata);
            return new ResponseEntity<>(fileId, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to upload file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // update file based on fileId
    @PutMapping("/{fileId}")
    public ResponseEntity<?> updateFile(@PathVariable Long fileId,
                                        @RequestParam(value = "file", required = false) MultipartFile file,
                                        @RequestParam(value = "fileName", required = false) String fileName) {
        
        if ((file == null)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty or not provided");
        }

        Optional<FileMetadata> optionalFileMetadata = fileService.getFileMetadataById(fileId);
        if (!optionalFileMetadata.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found for fileId: " + fileId);
        }

        try {
            String updatedStatus = fileService.updateFile(fileId, file, fileName);
            return ResponseEntity.ok(updatedStatus);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update file: " + e.getMessage());
        }
    }

    // delete file based on fileId
    @DeleteMapping("/{fileId}")
    public ResponseEntity<?> deleteFile(@PathVariable Long fileId){
        Optional<FileMetadata> optionalFileMetadata = fileService.getFileMetadataById(fileId);
        if (!optionalFileMetadata.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found for fileId: " + fileId);
        }

        try {
            String deleteStatus = fileService.deleteFile(fileId);
            return ResponseEntity.ok(deleteStatus);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete file");
        }
        
    }
    
}

