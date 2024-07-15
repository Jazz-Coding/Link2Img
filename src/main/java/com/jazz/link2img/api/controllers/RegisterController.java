package com.jazz.link2img.api.controllers;

import com.jazz.link2img.api.model.AuthReply;
import com.jazz.link2img.api.model.UserDetails;
import com.jazz.link2img.api.services.LoginService;
import com.jazz.link2img.api.services.RegisterService;
import com.jazz.link2img.api.services.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles user register endpoint.
 */
@RestController
@CrossOrigin
public class RegisterController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private RegisterService registerService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<AuthReply> register(@RequestBody UserDetails userDetails, HttpServletResponse response){
        String result = registerService.handleRegister(userDetails);
        if(result.equals(registerService.getErrorCode())){
            log.error("Failed to register.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthReply(result,false));
        } else {
            // Automatically log-in as well, the client can know whether to expect a useable auth token from the "success" boolean.
            result = tokenService.generateToken(userDetails.getUsername());

            // Include a set-cookie for the authToken.
            Cookie cookie = tokenService.storeInCookie(result);
            response.addCookie(cookie);

            log.info("Registered successfully. Replying with: {}", result);
            return ResponseEntity.ok(new AuthReply(result,true));
        }
    }
}
