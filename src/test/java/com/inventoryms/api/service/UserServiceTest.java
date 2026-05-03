package com.inventoryms.api.service;

import com.inventoryms.api.dto.user.CreateUserRequest;
import com.inventoryms.api.dto.user.UserRequest;
import com.inventoryms.api.dto.user.UserResponse;
import com.inventoryms.api.entity.Role;
import com.inventoryms.api.entity.User;
import com.inventoryms.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testuser");
        testUser.setPassword("encodedpassword");
        testUser.setRole(Role.SME_STAFF);
        testUser.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void getAllUsers_ShouldReturnListOfUserResponses() {
        when(userRepository.findAll()).thenReturn(List.of(testUser));

        List<UserResponse> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).getUsername());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUserResponse() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));

        UserResponse result = userService.getUserById(1);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldThrowException() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getUserById(99));

        assertEquals("User not found.", exception.getMessage());
        verify(userRepository, times(1)).findById(99);
    }

    @Test
    void update_WhenUserExists_ShouldUpdateAndReturnUserResponse() {
        UserRequest updateRequest = new UserRequest("updateduser", Role.SME_OWNER);
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        UserResponse result = userService.update(1, updateRequest);

        assertNotNull(result);
        assertEquals("updateduser", result.getUsername());
        assertEquals(Role.SME_OWNER.name(), result.getRole());
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void update_WhenUserDoesNotExist_ShouldThrowException() {
        UserRequest updateRequest = new UserRequest("updateduser", Role.SME_OWNER);
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.update(99, updateRequest));

        assertEquals("User not found.", exception.getMessage());
        verify(userRepository, times(1)).findById(99);
        verify(userRepository, never()).save(any());
    }

    @Test
    void create_WhenUsernameDoesNotExist_ShouldCreateAndReturnUserResponse() {
        CreateUserRequest createRequest = new CreateUserRequest("newuser", "password123", Role.SME_STAFF);
        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedpassword");
        
        User savedUser = new User();
        savedUser.setId(2);
        savedUser.setUsername("newuser");
        savedUser.setPassword("encodedpassword");
        savedUser.setRole(Role.SME_STAFF);
        savedUser.setCreatedAt(LocalDateTime.now());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse result = userService.create(createRequest);

        assertNotNull(result);
        assertEquals("newuser", result.getUsername());
        verify(userRepository, times(1)).findByUsername("newuser");
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void create_WhenUsernameAlreadyExists_ShouldThrowException() {
        CreateUserRequest createRequest = new CreateUserRequest("testuser", "password123", Role.SME_STAFF);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.create(createRequest));

        assertEquals("User already exists", exception.getMessage());
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }
}
