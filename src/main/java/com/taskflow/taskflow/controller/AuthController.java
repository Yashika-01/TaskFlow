package com.taskflow.taskflow.controller;

import com.taskflow.taskflow.security.JwtUtil;
import com.taskflow.taskflow.model.User;
import com.taskflow.taskflow.service.AuditLogService;
import com.taskflow.taskflow.service.CustomUserDetailsService;
import com.taskflow.taskflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final AuditLogService auditService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          CustomUserDetailsService userDetailsService, UserRepository userRepository,
                          AuditLogService auditService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.auditService = auditService;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestParam String username, @RequestParam String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        System.out.println("Authorities: " + authentication.getAuthorities());

        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Role not found"));

        String accessToken = jwtUtil.generateToken(username, role);
        String refreshToken = jwtUtil.generateRefreshToken(username, role);

        Map<String, String> response = new HashMap<>();
        response.put("accesstoken", accessToken);
        response.put("refreshToken", refreshToken);

        return response;
    }

    @PostMapping("/refresh")
    public Map<String, String> refreshToken(@RequestParam String refreshToken) {
        String username = jwtUtil.extractUsername(refreshToken);
        String role = jwtUtil.extractRole(refreshToken);

        if (jwtUtil.validateToken(refreshToken, username, role)) {
            String newAccessToken = jwtUtil.generateToken(username, role);

            Map<String, String> response = new HashMap<>();
            response.put("accessToken", newAccessToken);

            return response;
        } else {
            throw new IllegalArgumentException("Invalid refresh token");
        }
    }

}
