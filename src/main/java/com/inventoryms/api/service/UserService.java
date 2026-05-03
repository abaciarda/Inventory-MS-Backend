package com.inventoryms.api.service;

import com.inventoryms.api.dto.user.CreateUserRequest;
import com.inventoryms.api.dto.user.UserRequest;
import com.inventoryms.api.dto.user.UserResponse;
import com.inventoryms.api.entity.User;
import com.inventoryms.api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(UserResponse::new).toList();
    }

    public UserResponse getUserById(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found."));

        return new UserResponse(user);
    }

    public UserResponse update(int id, UserRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found."));

        user.setUsername(request.username());
        user.setRole(request.role());

        User updatedUser = userRepository.save(user);

        return new UserResponse(updatedUser);
    }

    public UserResponse create(CreateUserRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();

        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role());

        User newUser = userRepository.save(user);

        return new UserResponse(newUser);
    }
}
