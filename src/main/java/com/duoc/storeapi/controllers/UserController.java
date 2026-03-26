package com.duoc.storeapi.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duoc.storeapi.dto.UserUpdateRequest;
import com.duoc.storeapi.models.User;
import com.duoc.storeapi.services.UserService;
import com.duoc.storeapi.utils.AuthUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(@RequestHeader(value="X-User-Role", required=false) String role) {
        AuthUtil.requireAdminRole(role);
        return ResponseEntity.ok(userService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id, @RequestHeader(value="X-User-Role", required=false) String role, @RequestHeader(value="X-User-Id", required=false) Long headerUserId) {
        AuthUtil.requireAdminOrOwner(headerUserId, role, id);
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestHeader(value="X-User-Role", required=false) String role, @RequestHeader(value="X-User-Id", required=false) Long headerUserId, @Valid @RequestBody UserUpdateRequest user) {
        AuthUtil.requireAdminOrOwner(headerUserId, role, id);
        return ResponseEntity.ok(userService.update(id, user));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, @RequestHeader(value="X-User-Role", required=false) String role, @RequestHeader(value="X-User-Id", required=false) Long headerUserId) {
        AuthUtil.requireAdminOrOwner(headerUserId, role, id);
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
