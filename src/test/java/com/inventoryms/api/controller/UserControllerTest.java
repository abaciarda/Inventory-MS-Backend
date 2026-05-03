package com.inventoryms.api.controller;

import com.inventoryms.api.dto.ApiResponse;
import com.inventoryms.api.dto.user.CreateUserRequest;
import com.inventoryms.api.dto.user.UserRequest;
import com.inventoryms.api.dto.user.UserResponse;
import com.inventoryms.api.entity.Role;
import com.inventoryms.api.entity.User;
import com.inventoryms.api.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserResponse testUserResponse;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setRole(Role.SME_STAFF);
        user.setCreatedAt(LocalDateTime.now());
        testUserResponse = new UserResponse(user);
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        when(userService.getAllUsers()).thenReturn(List.of(testUserResponse));

        ResponseEntity<ApiResponse<List<UserResponse>>> responseEntity = userController.getAllUsers();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody().isSuccess());
        assertEquals("Users fetched successfully", responseEntity.getBody().getMessage());
        assertEquals(1, responseEntity.getBody().getData().size());
        
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUserById_ShouldReturnUser() {
        when(userService.getUserById(1)).thenReturn(testUserResponse);

        ResponseEntity<ApiResponse<UserResponse>> responseEntity = userController.getUserById(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody().isSuccess());
        assertEquals("User fetched successfully", responseEntity.getBody().getMessage());
        assertEquals("testuser", responseEntity.getBody().getData().getUsername());

        verify(userService, times(1)).getUserById(1);
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() {
        UserRequest request = new UserRequest("updateduser", Role.SME_OWNER);
        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setUsername("updateduser");
        updatedUser.setRole(Role.SME_OWNER);
        UserResponse updatedResponse = new UserResponse(updatedUser);

        when(userService.update(eq(1), any(UserRequest.class))).thenReturn(updatedResponse);

        ResponseEntity<ApiResponse<UserResponse>> responseEntity = userController.updateUser(1, request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody().isSuccess());
        assertEquals("User updated successfully", responseEntity.getBody().getMessage());
        assertEquals("updateduser", responseEntity.getBody().getData().getUsername());

        verify(userService, times(1)).update(1, request);
    }

    @Test
    void createUser_ShouldReturnCreatedUser() {
        CreateUserRequest request = new CreateUserRequest("newuser", "pass", Role.SME_STAFF);
        User newUser = new User();
        newUser.setId(2);
        newUser.setUsername("newuser");
        newUser.setRole(Role.SME_STAFF);
        UserResponse newResponse = new UserResponse(newUser);

        when(userService.create(any(CreateUserRequest.class))).thenReturn(newResponse);

        ResponseEntity<ApiResponse<UserResponse>> responseEntity = userController.createUser(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody().isSuccess());
        assertEquals("User created successfully", responseEntity.getBody().getMessage());
        assertEquals("newuser", responseEntity.getBody().getData().getUsername());

        verify(userService, times(1)).create(request);
    }
}
