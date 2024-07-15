package com.jazz.link2img.api.services;

import com.jazz.link2img.api.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
@Service
public class LocalStorageService {

    // We maintain two directories, one for public uploads and one for private uploads (which require authorization).
    private final String UPLOAD_DIR_PRIVATE = "static/private/uploads/";
    private final String UPLOAD_DIR_PUBLIC = "static/public/uploads/";

    private File UPLOAD_DIR_PRIVATE_FILE;
    private File UPLOAD_DIR_PUBLIC_FILE;

    public LocalStorageService() {
        File file1 = new File(UPLOAD_DIR_PRIVATE);
        if(!file1.exists()) file1.mkdirs();
        UPLOAD_DIR_PRIVATE_FILE = file1;

        File file2 = new File(UPLOAD_DIR_PUBLIC);
        if(!file2.exists()) file2.mkdirs();
        UPLOAD_DIR_PUBLIC_FILE = file2;
    }

    public Resource getFile(String filename, boolean isPublic){
        File parent = (isPublic ? UPLOAD_DIR_PUBLIC_FILE : UPLOAD_DIR_PRIVATE_FILE);
        File child = new File(parent.getAbsolutePath() + "/" + filename);
        return new FileSystemResource(child);
    }

    public void saveFile(MultipartFile file, String filename, boolean isPublic) throws IOException {
        File parent = (isPublic ? UPLOAD_DIR_PUBLIC_FILE : UPLOAD_DIR_PRIVATE_FILE);
        File child = new File(parent.getAbsolutePath() + "/" + filename);
        Files.copy(file.getInputStream(),child.toPath());
    }
}
