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
            if(userRepository.count() == 0) {
                User user1 = new User();
                user1.setName("User1");
                user1.setPassword("Passwort1");
                user1.setEmail("user1@example.com");
                user1.setUserRole(UserRole.CROOK);
                
                Family family1 = new Family();
                family1.setFamilyName("BRUHF");
                user1.setFamily(family1);
                
                userRepository.save(user1);
            }
        };
    }
}
