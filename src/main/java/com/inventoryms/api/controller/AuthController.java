package com.inventoryms.api.controller;

import com.inventoryms.api.dto.ErrorResponse;
import com.inventoryms.api.dto.MessageResponse;
import com.inventoryms.api.dto.auth.AuthResponse;
import com.inventoryms.api.dto.auth.LoginRequest;
import com.inventoryms.api.dto.auth.LoginResult;
import com.inventoryms.api.entity.User;
import com.inventoryms.api.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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

            AuthResponse response = new AuthResponse(true, user.getId(), user.getUsername(), user.getRole().name());

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

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(new MessageResponse("Logged out successfuly"));
    }
}
