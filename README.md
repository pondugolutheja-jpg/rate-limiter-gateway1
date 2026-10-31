# Rate Limiter & API Gateway

A rate limiting and abuse protection service built with Java and Spring Boot. It limits requests per client IP and blocks brute-force login attempts.

## Features
- **Token bucket** limiter: allows short bursts while enforcing an average rate
- **Sliding window** limiter: strict request count over a rolling time window
- **Switchable algorithm** with one config line (`ratelimit.algorithm`)
- **Brute-force protection**: IP is banned for 5 minutes after 5 failed logins
- Returns HTTP `429` when the rate limit is exceeded and `403` for banned IPs

## Tech
Java 17+, Spring Boot, Maven

## Run
1. Clone the repo
2. Run `RatelimiterApplication`
3. Open `http://localhost:8081/api/test`

## Roadmap
- Redis-backed counters so limits are shared across multiple servers
- Docker Compose with Nginx load balancing
- Load test results (k6) with benchmarks