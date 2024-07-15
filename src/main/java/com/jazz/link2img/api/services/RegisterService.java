package com.jazz.link2img.api.services;

import com.jazz.link2img.api.model.UserDetails;
import com.jazz.link2img.api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegisterService {
    private static final Logger log = LoggerFactory.getLogger(RegisterService.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String handleRegister(UserDetails userDetails) {
        String username = userDetails.getUsername();
        Optional<UserDetails> userInDb = userRepository.findById(username);
        if(userInDb.isPresent()){
            log.info("Failed to register. User already exists.");
            return getErrorCode();
        } else {
            String password = passwordEncoder.encode(userDetails.getPassword());
            userDetails.setPassword(password); // Overwrite with the hashed password.
            userRepository.save(userDetails); // Save to the database.
            log.info("Registered user {} successfully.", username);
            return "success";
        }
    }

    public String getErrorCode() {
        return "ERROR";
    }
}
