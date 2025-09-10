package com.example.quoteapi;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.quoteapi.service.RateLimitService;

import io.github.bucket4j.Bucket;

@SpringBootTest
public class RateLimitServiceTest {
    @Autowired
    private RateLimitService rateLimitService;

    @Test
    public void testRateLimit() {
        String ip = "127.0.0.1";
        Bucket bucket = rateLimitService.getBucket(ip);

        for (int i = 0; i < 5; i++) {
            assertTrue(bucket.tryConsume(1), "Should allow request " + (i + 1));
        }

        assertFalse(bucket.tryConsume(1), "Should block 6th request");
    }
}
