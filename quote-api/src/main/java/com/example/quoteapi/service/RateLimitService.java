package com.example.quoteapi.service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

@Service
public class RateLimitService {
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final long capacity;
    private final Duration refillDuration;

    public RateLimitService(@Value("${rate.limit.capacity}") long capacity,
            @Value("${rate.limit.refill}") String refillDuration) {
		this.capacity = capacity;
		// Example: "5s" or "1m"
		char timeUnit = refillDuration.charAt(refillDuration.length() - 1);
		long time = Long.parseLong(refillDuration.substring(0, refillDuration.length() - 1));
		switch (timeUnit) {
		case 's' -> this.refillDuration = Duration.ofSeconds(time);
		case 'm' -> this.refillDuration = Duration.ofMinutes(time);
		case 'h' -> this.refillDuration = Duration.ofHours(time);
		default -> throw new IllegalArgumentException("Invalid refill duration: " + refillDuration);
		}
	}


    public Bucket getBucket(String ipAddress) {
        return buckets.computeIfAbsent(ipAddress, k -> {
            Refill refill = Refill.intervally(capacity, refillDuration);
            Bandwidth limit = Bandwidth.classic(capacity, refill);
            return Bucket.builder().addLimit(limit).build();
        });
    }
}