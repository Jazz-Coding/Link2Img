package com.jazz.link2img.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileIDService {
    @Autowired
    private RandomService randomService;

    public String generateID(MultipartFile file){
        int length = 16;
        return randomService.randomID(length);
    }
}
