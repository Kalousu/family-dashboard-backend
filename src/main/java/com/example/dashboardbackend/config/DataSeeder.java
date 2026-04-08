package com.example.dashboardbackend.config;

import com.example.dashboardbackend.models.Dashboard;
import com.example.dashboardbackend.models.Family;
import com.example.dashboardbackend.models.User;
import com.example.dashboardbackend.models.enums.UserRole;
import com.example.dashboardbackend.models.widgets.WidgetConfig;
import com.example.dashboardbackend.models.widgets.WidgetItem;
import com.example.dashboardbackend.repositories.DashboardRepository;
import com.example.dashboardbackend.repositories.FamilyRepository;
import com.example.dashboardbackend.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.*;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, DashboardRepository dashboardRepository, FamilyRepository familyRepository) {
        return args -> {
            if(userRepository.count() == 0) {
                Family family1 = new Family();
                family1.setFamilyName("BRUHF");

                User user1 = new User();
                user1.setName("User1");
                user1.setPassword("Passwort1");
                user1.setEmail("user1@example.com");
                user1.setUserRole(UserRole.CROOK);

                Dashboard dashboard1 = new Dashboard();

                WidgetItem weatherWidget = new WidgetItem();
                weatherWidget.setType("weather");
                weatherWidget.setCreatedAt(LocalDateTime.now());
                weatherWidget.setDashboard(dashboard1);

                Map<String, Object> weatherSettings = new HashMap<>();
                weatherSettings.put("city", "Mannheim");
                weatherSettings.put("latitude", 49.4891);
                weatherSettings.put("longitude", 8.46694);
                weatherSettings.put("timezone", "Europe/Berlin");

                weatherWidget.setWidgetConfig(new WidgetConfig(
                        "Wetter Mannheim",
                        "blue",
                        weatherSettings
                ));

                dashboard1.setWidgetItems(List.of(weatherWidget));

                Family savedFamily = familyRepository.save(family1);

                user1.setFamily(savedFamily);
                userRepository.save(user1);

                dashboard1.setFamily(savedFamily);
                dashboardRepository.save(dashboard1);
            }
        };
    }
}
