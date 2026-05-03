package com.inventoryms.api.controller;

import com.inventoryms.api.dto.user.CreateUserRequest;
import com.inventoryms.api.dto.user.UserRequest;
import com.inventoryms.api.dto.user.UserResponse;
import com.inventoryms.api.dto.ApiResponse;
import com.inventoryms.api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        ApiResponse<List<UserResponse>> response = new ApiResponse<>(true, "Users fetched successfully", userService.getAllUsers());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable int id) {
        ApiResponse<UserResponse> response = new ApiResponse<>(true, "User fetched successfully", userService.getUserById(id));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable int id, @Valid @RequestBody UserRequest request) {
        ApiResponse<UserResponse> response = new ApiResponse<>(true, "User updated successfully", userService.update(id, request));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody CreateUserRequest request) {
        ApiResponse<UserResponse> response = new ApiResponse<>(true, "User created successfully", userService.create(request));
        return ResponseEntity.ok(response);
    }

}
