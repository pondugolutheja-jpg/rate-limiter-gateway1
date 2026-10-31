package com.Thejaswini.ratelimiter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    @Value("${ratelimit.algorithm:token-bucket}")
    private String algorithm;

    private final ConcurrentHashMap<String, TokenBucket> tokenBuckets = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, SlidingWindowLimiter> windows = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String ip = request.getRemoteAddr();
        boolean allowed;

        if (algorithm.equals("sliding-window")) {
            allowed = windows.computeIfAbsent(ip, k -> new SlidingWindowLimiter(100, 60_000)).tryConsume();
        } else {
            allowed = tokenBuckets.computeIfAbsent(ip, k -> new TokenBucket(100, 100.0 / 60)).tryConsume();
        }

        if (allowed) {
            chain.doFilter(request, response);
        } else {
            response.setStatus(429);
            response.getWriter().write("Too many requests");
        }
    }
}