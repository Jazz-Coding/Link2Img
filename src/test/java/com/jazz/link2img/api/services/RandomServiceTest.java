package com.jazz.link2img.api.services;

import com.jazz.link2img.Application;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest (classes = Application.class)
@AutoConfigureMockMvc
class RandomServiceTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private RandomService randomService;

    @Test
    void testNoCollisions() {
        Set<String> ids = new HashSet<>();

        int n = 1_000_000;
        for (int i = 0; i < n; i++) {
            ids.add(randomService.randomID(16));
        }
        assertEquals(ids.size(), n); // If fewer ids are found, a collision must have occurred due to the nature of "Set".
    }
}