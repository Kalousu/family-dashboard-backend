package com.example.dashboardbackend.config;

import com.example.dashboardbackend.models.Family;
import com.example.dashboardbackend.models.User;
import com.example.dashboardbackend.models.enums.UserRole;
import com.example.dashboardbackend.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            // Add sample products to the database, if empty
            if(userRepository.count() == 0) {
                userRepository.save(new User(null, "User1", "Passwort1", "EMAIL", UserRole.CROOK,
                        new Family(null, "BRUHF")
                ));
            }
        };
    }
}
