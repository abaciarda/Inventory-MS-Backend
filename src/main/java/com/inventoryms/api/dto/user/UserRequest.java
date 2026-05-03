package com.inventoryms.api.dto.user;

import com.inventoryms.api.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank(message = "Username cant be empty.")
        @Size(min = 3, max = 80)
        String username,

        @NotNull(message = "Role cant be empty.")
        Role role
) {}