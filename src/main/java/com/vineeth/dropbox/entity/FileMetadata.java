package com.vineeth.dropbox.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "file_metadata")
public class FileMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @ManyToOne
    // @JoinColumn(name = "user_id", nullable = false)
    // private User user;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(nullable = false)
    private long size;

    @Column(name = "file_type", nullable = false)
    private String fileType;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
}
