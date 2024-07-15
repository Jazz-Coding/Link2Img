package com.jazz.link2img.api.controllers;

import com.jazz.link2img.api.model.FileEntry;
import com.jazz.link2img.api.repository.FileRepository;
import com.jazz.link2img.api.services.DownloadService;
import com.jazz.link2img.api.services.TokenService;
import com.jazz.link2img.api.services.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin
public class MainController {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private UploadService uploadService;

    @Autowired
    private DownloadService downloadService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private FileRepository fileRepository;

    private boolean fileExists(String filename){
        return fileRepository.existsById(filename);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file, @RequestParam("isPublic") boolean isPublic, @RequestHeader("Authorization") String authorization){
        String result;

        if(isPublic){
            result = uploadService.handleFileUpload(file, true, "");
        } else {
            // Validate the authorization.
            boolean valid = tokenService.checkTokenHeader(authorization);
            if(!valid) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

            // Proceed.
            String username = tokenService.extractUsernameHeader(authorization);
            result = uploadService.handleFileUpload(file, false, username);
        }

        if(result.equals(uploadService.getErrorCode())){ // If unsuccessful, return an error.
            log.error("Failed to upload file.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
        } else { // Otherwise, if successful, the result contains the link to access the file.
            log.info("Uploaded file successfully. Link: {}", result);
            return ResponseEntity.ok(result);
        }
    }

    @GetMapping("/private/uploads/{filename}")
    public ResponseEntity<byte[]> downloadPrivate(@PathVariable String filename, @RequestHeader("Authorization") String authorization){
        // Validate the authorization of requests to /private.

        // File actually exists?
        boolean fileExists = fileExists(filename);
        if(!fileExists) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        // Token hasn't expired?
        boolean tokenFresh = tokenService.checkTokenHeader(authorization);
        if(!tokenFresh) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        // User has access to this file?
        String username = tokenService.extractUsernameHeader(authorization);

        FileEntry fileEntry = fileRepository.findById(filename).get();
        boolean userHasAccess = fileEntry.getOwnerUsername().equals(username);
        if(!userHasAccess) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        // If all of the above are true, serve the request.
        Resource resource = downloadService.getFileAsResource(filename, false);
        try {
            long contentLength = resource.contentLength();

            // Serve the file for download
            return ResponseEntity.ok()
                    .contentLength(contentLength)
                    .header("Content-Type", MediaType.IMAGE_PNG.toString())
                    .body(resource.getContentAsByteArray());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/public/uploads/{filename}")
    public ResponseEntity<byte[]> downloadPublic(@PathVariable String filename){
        // Serve any requests to /public.
        Resource resource = downloadService.getFileAsResource(filename, true);
        try {
            long contentLength = resource.contentLength();

            // Serve the file for download
            return ResponseEntity.ok()
                    .contentLength(contentLength)
                    .header("Content-Type", MediaType.IMAGE_PNG.toString())
                    .body(resource.getContentAsByteArray());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
