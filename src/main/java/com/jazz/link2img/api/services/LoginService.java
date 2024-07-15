package com.jazz.link2img.api.services;

import com.jazz.link2img.api.model.UserDetails;
import com.jazz.link2img.api.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // We use BCrypt

    public String handleLogin(UserDetails userDetails) {
        String username = userDetails.getUsername();
        String password = userDetails.getPassword();

        log.info("Attempting log-in in with: {}:********", username);

        Optional<UserDetails> userInDb = userRepository.findById(username);
        if(userInDb.isPresent()){
            String passwordInDb = userInDb.get().getPassword();
            if(passwordEncoder.matches(password, passwordInDb)){
                log.info("Successful.");
                String authToken = tokenService.generateToken(username);
                return authToken;
            } else {
                log.info("Incorrect password.");
                return getErrorCode();
            }
        } else {
            log.info("User does not exist.");
            return getErrorCode();
        }
    }

    public String getErrorCode() {
        return "ERROR";
    }
}
