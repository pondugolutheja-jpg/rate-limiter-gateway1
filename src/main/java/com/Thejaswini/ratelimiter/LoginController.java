package com.Thejaswini.ratelimiter;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final BruteForceProtector protector;

    public LoginController(BruteForceProtector protector) {
        this.protector = protector;
    }

    @GetMapping("/api/login")
    public ResponseEntity<String> login(@RequestParam String username,
                                        @RequestParam String password,
                                        HttpServletRequest request) {
        String ip = request.getRemoteAddr();

        if (protector.isBanned(ip)) {
            return ResponseEntity.status(403).body("IP banned for too many failed logins");
        }
        if (username.equals("admin") && password.equals("secret123")) {
            protector.recordSuccess(ip);
            return ResponseEntity.ok("Login successful");
        }
        protector.recordFailure(ip);
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}