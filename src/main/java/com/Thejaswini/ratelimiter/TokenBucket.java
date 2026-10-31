package com.Thejaswini.ratelimiter;

public class TokenBucket {
    private final long capacity;
    private final double refillPerSecond;
    private double tokens;
    private long lastRefillTime;

    public TokenBucket(long capacity, double refillPerSecond) {
        this.capacity = capacity;
        this.refillPerSecond = refillPerSecond;
        this.tokens = capacity;
        this.lastRefillTime = System.nanoTime();
    }

    public synchronized boolean tryConsume() {
        refill();
        if (tokens >= 1) {
            tokens -= 1;
            return true;
        }
        return false;
    }

    private void refill() {
        long now = System.nanoTime();
        double secondsPassed = (now - lastRefillTime) / 1_000_000_000.0;
        tokens = Math.min(capacity, tokens + secondsPassed * refillPerSecond);
        lastRefillTime = now;
    }
}
