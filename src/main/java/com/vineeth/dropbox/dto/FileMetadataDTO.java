package com.vineeth.dropbox.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.List;

import com.vineeth.dropbox.entity.FileMetadata;

@Getter
@Setter
public class FileMetadataDTO {
    private Long id;
    private String fileName;
    private Date createdAt;
    private long size;
    private String fileType;

    public static FileMetadataDTO fromEntity(FileMetadata fileMetadata) {
        FileMetadataDTO dto = new FileMetadataDTO();
        dto.setId(fileMetadata.getId());
        dto.setFileName(fileMetadata.getFileName());
        dto.setCreatedAt(fileMetadata.getCreatedAt());
        dto.setSize(fileMetadata.getSize());
        dto.setFileType(fileMetadata.getFileType());
        return dto;
    }

    public static List<FileMetadataDTO> fromEntityList(List<FileMetadata> fileMetadataList) {
        return fileMetadataList.stream()
                .map(FileMetadataDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
