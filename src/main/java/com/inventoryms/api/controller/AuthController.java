package com.inventoryms.api.controller;

import com.inventoryms.api.dto.ErrorResponse;
import com.inventoryms.api.dto.ApiResponse;
import com.inventoryms.api.dto.auth.LoginRequest;
import com.inventoryms.api.dto.auth.LoginResult;
import com.inventoryms.api.dto.user.UserResponse;
import com.inventoryms.api.entity.User;
import com.inventoryms.api.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest servletRequest) {
        try {
            LoginResult result = authService.login(request);
            User user = result.getUser();

            ResponseCookie cookie = ResponseCookie.from("access_token", result.getToken())
                    .httpOnly(true)
                    .secure(false)
                    .sameSite("Lax")
                    .path("/")
                    .maxAge(Duration.ofDays(1))
                    .build();

            UserResponse userResponse = new UserResponse(user);
            ApiResponse<UserResponse> response = new ApiResponse<>(true, "Login successful", userResponse);

            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(response);
        } catch(BadCredentialsException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(
                    false,
                    "Invalid username or password.",
                    HttpStatus.UNAUTHORIZED.value(),
                    servletRequest.getRequestURI()
            ));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();

        ApiResponse<Void> response = new ApiResponse<>(true, "Logged out successfully", null);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = (User) authentication.getPrincipal();
        UserResponse userResponse = new UserResponse(user);
        ApiResponse<UserResponse> response = new ApiResponse<>(true, "User fetched successfully", userResponse);
        
        return ResponseEntity.ok(response);
    }
}
