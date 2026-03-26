package com.duoc.storeapi.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duoc.storeapi.dto.LoginRequest;
import com.duoc.storeapi.models.User;
import com.duoc.storeapi.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        User user = userService.login(request.getEmail(), request.getPassword());
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login exitoso");
        response.put("userId", user.getId());
        return ResponseEntity.ok(response);
    }
}
