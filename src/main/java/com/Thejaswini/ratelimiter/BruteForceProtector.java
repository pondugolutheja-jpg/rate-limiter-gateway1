package com.Thejaswini.ratelimiter;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class BruteForceProtector {
    private static final int MAX_FAILURES = 5;
    private static final long BAN_MILLIS = 5 * 60 * 1000;

    private final ConcurrentHashMap<String, Integer> failures = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> bannedUntil = new ConcurrentHashMap<>();

    public boolean isBanned(String ip) {
        Long until = bannedUntil.get(ip);
        if (until == null) return false;
        if (System.currentTimeMillis() > until) {
            bannedUntil.remove(ip);
            failures.remove(ip);
            return false;
        }
        return true;
    }

    public void recordFailure(String ip) {
        int count = failures.merge(ip, 1, Integer::sum);
        if (count >= MAX_FAILURES) {
            bannedUntil.put(ip, System.currentTimeMillis() + BAN_MILLIS);
            System.out.println("BANNED IP: " + ip);
        }
    }

    public void recordSuccess(String ip) {
        failures.remove(ip);
    }
}