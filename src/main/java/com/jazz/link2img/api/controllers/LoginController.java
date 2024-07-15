package com.jazz.link2img.api.controllers;

import com.jazz.link2img.api.model.AuthReply;
import com.jazz.link2img.api.model.UserDetails;
import com.jazz.link2img.api.services.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<AuthReply> login(@RequestBody UserDetails userDetails){
        String result = loginService.handleLogin(userDetails);
        if(result.equals(loginService.getErrorCode())){ // If unsuccessful, return an error.
            log.error("Failed to log in.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthReply(result,false));
        } else { // Otherwise, if successful, the result contains the link to access the file.
            log.info("Logged in successfully. Replying with: {}", result);
            return ResponseEntity.ok(new AuthReply(result,true));
        }
    }
}
