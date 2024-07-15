package com.jazz.link2img.api.services;

import com.jazz.link2img.api.model.FileEntry;
import com.jazz.link2img.api.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadService {
    @Autowired
    private FileIDService fileIDService;

    @Autowired
    private LocalStorageService localStorageService;

    @Autowired
    private FileRepository fileRepository;

    private final String ERROR_CODE = "ERROR";

    private static final List<String> VALID_FILE_TYPES = new ArrayList<>(Arrays.asList(
            "image/png",
            "image/jpeg",
            "image/gif"));

    private boolean fileValid(MultipartFile file){
        return VALID_FILE_TYPES.contains(file.getContentType());
    }

    public String handleFileUpload(MultipartFile file, boolean isPublic, String username){
        try {
            if(!fileValid(file)){
                return getErrorCode();
            }
            String originalName = file.getOriginalFilename();
            String extension = originalName.substring(originalName.lastIndexOf(".") + 1);
            String filename = fileIDService.generateID(file) + "." + extension;

            localStorageService.saveFile(file, filename, isPublic);

            if(!isPublic){
                // Also create an entry in the table under this username.
                fileRepository.save(new FileEntry(filename, username));
            }

            // Return the associated URL.
            String baseUrl = isPublic ? "https://link2img.net/public/uploads/" : "https://link2img.net/private/uploads/";
            String destinationURL = baseUrl + filename;
            return destinationURL;
        } catch (IOException e) {
            e.printStackTrace();
            return ERROR_CODE;
        }
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
