package com.vineeth.dropbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vineeth.dropbox.entity.*;

@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {
}

