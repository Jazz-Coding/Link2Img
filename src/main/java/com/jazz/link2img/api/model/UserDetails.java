package com.jazz.link2img.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import org.springframework.stereotype.Component;

@Entity
@Component
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class UserDetails {
    @Id
    private String username;
    private String password;
}
