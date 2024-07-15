package com.jazz.link2img.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Entity
@Component
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class FileEntry {
    // Create a table with rows like:
    /*
        file1.png | user1
        file2.png | user1
        file3.png | user2
     */
    // Then we can check if "requestedFile[ownerUsername]" == the user who is requesting the file.
    @Id
    private String filename;
    private String ownerUsername;
}
