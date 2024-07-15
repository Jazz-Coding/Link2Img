package com.jazz.link2img.api.repository;

import com.jazz.link2img.api.model.FileEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileEntry, String> {
}
