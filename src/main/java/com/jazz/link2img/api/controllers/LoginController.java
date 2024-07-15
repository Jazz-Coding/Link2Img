package com.jazz.link2img.api.controllers;

import com.jazz.link2img.api.model.AuthReply;
import com.jazz.link2img.api.model.UserDetails;
import com.jazz.link2img.api.services.LoginService;
import com.jazz.link2img.api.services.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Handles user login endpoint.
 */
@RestController
@CrossOrigin
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<AuthReply> login(@RequestBody UserDetails userDetails, HttpServletResponse response){
        String result = loginService.handleLogin(userDetails);
        if(result.equals(loginService.getErrorCode())){ // If unsuccessful, return an error.
            log.error("Failed to log in.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthReply(result,false));
        } else {
            log.info("Logged in successfully. Replying with: {}", result);

            // Include a set-cookie for the authToken.
            Cookie cookie = tokenService.storeInCookie(result);
            response.addCookie(cookie);

            return ResponseEntity.ok(new AuthReply(result,true));
        }
    }

   /* @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response){
        *//*HttpSession session = request.getSession(false);
        session.invalidate();

        SecurityContext context = SecurityContextHolder.getContext();
        SecurityContextHolder.clearContext();*//*

        *//*context.setAuthentication(null);*//*
        response.addCookie(tokenService.nullCookie());
        return ResponseEntity.ok("Logged out.");
    }*/
}
