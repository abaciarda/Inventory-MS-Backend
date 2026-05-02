package com.inventoryms.api.config;

import com.inventoryms.api.entity.Role;
import com.inventoryms.api.entity.User;
import com.inventoryms.api.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedUsers(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (!userRepository.existsByUsername("arda")) {
                User owner = new User();
                owner.setUsername("arda");
                owner.setPassword(passwordEncoder.encode("123456"));
                owner.setRole(Role.SME_OWNER);

                userRepository.save(owner);
            }

            if (!userRepository.existsByUsername("staff")) {
                User staff = new User();
                staff.setUsername("staff");
                staff.setPassword(passwordEncoder.encode("123456"));
                staff.setRole(Role.SME_STAFF);

                userRepository.save(staff);
            }
        };
    }
}