package com.jazz.link2img.api.services;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Random;

@Service
public class RandomService {
    private Random RNG = new SecureRandom();
    private byte[] alphabet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".getBytes(StandardCharsets.UTF_8);

    public String randomID(int length){
        byte[] randomBytes = new byte[length];
        for (int i = 0; i < length; i++) {
            randomBytes[i] = alphabet[RNG.nextInt(alphabet.length)];
        }
        return new String(randomBytes);
    }
}
