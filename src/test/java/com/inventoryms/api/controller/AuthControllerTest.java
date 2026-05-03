package com.inventoryms.api.controller;

import com.inventoryms.api.dto.ApiResponse;
import com.inventoryms.api.dto.ErrorResponse;
import com.inventoryms.api.dto.auth.LoginRequest;
import com.inventoryms.api.dto.auth.LoginResult;
import com.inventoryms.api.dto.user.UserResponse;
import com.inventoryms.api.entity.Role;
import com.inventoryms.api.entity.User;
import com.inventoryms.api.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private AuthController authController;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testuser");
        testUser.setRole(Role.SME_STAFF);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void login_WithValidCredentials_ShouldReturnOkWithCookie() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        LoginResult loginResult = new LoginResult("mocked.token", testUser);

        when(authService.login(any(LoginRequest.class))).thenReturn(loginResult);
        when(request.getHeader(HttpHeaders.ORIGIN)).thenReturn("http://localhost:3000");

        ResponseEntity<?> responseEntity = authController.login(loginRequest, request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getHeaders().getFirst(HttpHeaders.SET_COOKIE) != null);
        String cookieValue = responseEntity.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        assertTrue(cookieValue != null && cookieValue.contains("access_token=mocked.token"));
        
        ApiResponse<?> apiResponse = (ApiResponse<?>) responseEntity.getBody();
        assertNotNull(apiResponse);
        assertTrue(apiResponse.isSuccess());
        assertEquals("Login successful", apiResponse.getMessage());
        
        verify(authService, times(1)).login(loginRequest);
    }

    @Test
    void login_WithInvalidCredentials_ShouldReturnUnauthorized() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("wrong");

        when(authService.login(any(LoginRequest.class))).thenThrow(new BadCredentialsException("Bad creds"));
        when(request.getRequestURI()).thenReturn("/api/auth/login");

        ResponseEntity<?> responseEntity = authController.login(loginRequest, request);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertNotNull(errorResponse);
        assertFalse(errorResponse.isSuccess());
        assertEquals("Invalid username or password.", errorResponse.getMessage());
        assertEquals(401, errorResponse.getStatus());
        assertEquals("/api/auth/login", errorResponse.getPath());
    }

    @Test
    void logout_ShouldReturnOkWithClearedCookie() {
        when(request.getHeader(HttpHeaders.ORIGIN)).thenReturn("http://localhost:3000");

        ResponseEntity<?> responseEntity = authController.logout(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getHeaders().getFirst(HttpHeaders.SET_COOKIE) != null);
        String cookieValue = responseEntity.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        assertTrue(cookieValue != null && cookieValue.contains("Max-Age=0"));
        assertTrue(cookieValue.contains("access_token="));

        ApiResponse<?> apiResponse = (ApiResponse<?>) responseEntity.getBody();
        assertNotNull(apiResponse);
        assertTrue(apiResponse.isSuccess());
        assertEquals("Logged out successfully", apiResponse.getMessage());
    }

    @Test
    void getCurrentUser_WhenAuthenticated_ShouldReturnUser() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        
        when(authentication.getPrincipal()).thenReturn(testUser);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        ResponseEntity<?> responseEntity = authController.getCurrentUser();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ApiResponse<?> apiResponse = (ApiResponse<?>) responseEntity.getBody();
        assertNotNull(apiResponse);
        assertTrue(apiResponse.isSuccess());
        UserResponse userResponse = (UserResponse) apiResponse.getData();
        assertEquals("testuser", userResponse.getUsername());
    }

    @Test
    void getCurrentUser_WhenNotAuthenticated_ShouldReturnUnauthorized() {
        SecurityContext securityContext = mock(SecurityContext.class);
        
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        ResponseEntity<?> responseEntity = authController.getCurrentUser();

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }
}
