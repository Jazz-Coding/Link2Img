package com.jazz.link2img.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class DownloadService {
    @Autowired
    private LocalStorageService localStorageService;

    public Resource getFileAsResource(String fileName, boolean isPublic){
        return localStorageService.getFile(fileName,isPublic);
    }
}
